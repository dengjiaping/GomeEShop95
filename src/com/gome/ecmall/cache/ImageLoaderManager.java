package com.gome.ecmall.cache;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.gome.ecmall.cache.BytesArrayFactory.BytesArray;
import com.gome.ecmall.cache.MemoryFileCache.CacheBlock;
import com.gome.ecmall.util.BDebug;

/**
 * 图片加载管理器
 */
public class ImageLoaderManager {
    private static final String TAG = "ImageCacheManager";
    /**
     * 图片【Bitmap】标识
     */
    public static final String MAP_KEY_BITMAP = "bitmap";
    /**
     * 下载任务标识
     */
    public static final String MAP_KEY_TASK = "task";
    /**
     * 加载完成
     */
    public static final int ACTION_LOAD_COMPLETED = 1;
    /**
     * 加载开始
     */
    public static final int ACTION_LOAD_START = 2;
    /**
     * 加载数量
     */
    public static final int ACTION_LOAD_COUNT = 3;
    /**
     * 加载结束
     */
    public static final int ACTION_LOAD_FINISH = 4;
    /**
     * 固定线程池大小【默认为3】
     */
    public static final int DEFAULT_CONCURRENT_THREAD_SIZE = 4;
    /**
     * 软引用最大数量
     */
    public static final int SOFT_REFERENCE_MAX_SIZE = 40;
    /**
     * 线程优先级-最小
     */
    public static final int TASK_PRIORITY_MIN = 1;
    /**
     * 线程优先级-适中
     */
    public static final int TASK_PRIORITY_MIDDLE = 2;
    /**
     * 线程优先级-最大
     */
    public static final int TASK_PRIORITY_MAX = 3;
    /**
     * 当前线程优先级【默认最小】
     */
    public int currentTaskPriority = TASK_PRIORITY_MIN;
    /**
     * LruCache可控回收力度
     */
    public LruCache<String, Bitmap> memoryCache;
    /**
     * 软引用
     */
    public ConcurrentHashMap<String, SoftReference<Bitmap>> softRefMap;
    /**
     * 任务列表
     */
    private LinkedList<ImageLoadTask> taskList = new LinkedList<ImageLoadTask>();
    /**
     * 虚拟内存标识位列表【ConcurrentHashMap 可提高并发效率】
     */
    private ConcurrentHashMap<String, CacheBlock> memoryFileMap = new ConcurrentHashMap<String, CacheBlock>();
    /**
     * 虚拟内存
     */
    private MemoryFileCache memoryFileCache;
    /**
     * 图片加载管理器
     */
    private static ImageLoaderManager loaderManager;
    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 
     * @param maxConcurrentThreads
     *            最大并发线程数
     * @param cacheCapacity
     *            内存文件缓存大小
     * @param virtualCacheSize
     *            虚拟内存缓存大小
     */
    private ImageLoaderManager(int maxConcurrentThreads, int memoryCacheSize, int virtualCacheSize) {
        //实例化一个LruCache
    	memoryCache = new LruCache<String, Bitmap>(memoryCacheSize) {
            /**
             * 当调用lruCache.get(String key)方法返回值为null时,会调用此create(String key)方法为此key创建一个Value
             */
            @Override
            protected Bitmap create(String key) {
                BDebug.i(TAG, "createKey:" + key);
                return super.create(key);
            }

            /**
             * 计算此Value在内存中占用的字节大小， 以此来衡量是否达到了LurCache的最大容量。 为在恰当时机释放之前的key-value提供依据
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value == null) {
                    return super.sizeOf(key, value);
                } else {
                    final int bytes = value.getRowBytes() * value.getHeight();
                    return bytes;
                }
            }

            /**
             * 超过LruCache最大容量，移除硬引用，为了回收利用，放入软引用表里面
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                softRefMap.put(key, new SoftReference<Bitmap>(oldValue));
            }
        };
        
        //实例化一个块虚拟内存
        try {
            memoryFileCache = new MemoryFileCache(virtualCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //实例化一个软引用列表
        softRefMap = new ConcurrentHashMap<String, SoftReference<Bitmap>>(SOFT_REFERENCE_MAX_SIZE);
        
        //实例化一个线程池
        executorService = Executors.newFixedThreadPool(maxConcurrentThreads);
    }

    /**
     * 单例获取一个 ImageLoaderManager
     * @param maxConcurrentThreads 线程池最大线程数
     * @param memoryCacheSize 内存大小
     * @param virtualCacheSize 虚拟内存大小
     * @return ImageLoaderManager
     */
    public synchronized static ImageLoaderManager getInstance(int maxConcurrentThreads, int memoryCacheSize,
            int virtualCacheSize) {
        if (loaderManager == null) {
            loaderManager = new ImageLoaderManager(maxConcurrentThreads, memoryCacheSize, virtualCacheSize);
        }
        return loaderManager;
    }

    /**
     * 获取默认 ImageLoaderManager
     * @return ImageLoaderManager
     */
    public static ImageLoaderManager getDefaultInstance() {
        return loaderManager;
    }

    /**
     * 初始化图片加载管理器
     * @param context 上下文
     * @return ImageLoaderManager
     */
    public static ImageLoaderManager initImageLoaderManager(Context context) {
        if (context == null) {
            throw new NullPointerException("context can't be null!");
        }
        if (loaderManager == null) {
        	
        	//获取应用activity管理器
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            
            //获取应用 运行 可用内存大小
            final int memoryClass = activityManager.getMemoryClass();
            
            //可用内存的1/16  --->  【Byte】
            final int memoryCacheSize = memoryClass / 16 * 1024 * 1024;
            
            //虚拟内存大小=【内存分配大小 * 2】
            final int virtualCacheSize = memoryCacheSize * 2;// MemoryFileCache.getRecommendMemoryFileSize(context);
            
            //实例化一个 图片加载管理器
            loaderManager = ImageLoaderManager.getInstance(DEFAULT_CONCURRENT_THREAD_SIZE, memoryCacheSize,
                    virtualCacheSize);
        }
        //初始化SDcard 缓存
        DiskCache.initDiskCache(context);
        return loaderManager;
    }

    /**
     * 设置后台任务的优先级
     * 
     * @param taskPriority
     */
    public void setTaskPriority(int taskPriority) {
        currentTaskPriority = taskPriority;
    }

    /**
     * 获取后台任务的优先级
     * 
     * @return
     */
    public int getTaskPriority() {
        return currentTaskPriority;
    }

    /**
     * 获取内存中的 Bitmap
     * @param url 图片地址
     * @return Bitmap
     */
    public Bitmap getCacheBitmap(String url) {
        if (url == null) {
            return null;
        }
        // 查找硬引用【Lrucache】
        Bitmap bitmap = memoryCache.get(url);
        
        //硬引用中无对应的Bitmap,继续查找软应用
        if (bitmap == null) {
        	//硬引用中移除key【url】
            memoryCache.remove(url);
            //从map中获取url对应的软引用
            SoftReference<Bitmap> sf = softRefMap.get(url);
            //如果软应用不为空
            if (sf != null) {
                bitmap = sf.get();
                //当软应用中的Bitmap为空
                if (bitmap == null) {
                	//移除软应用列表中key【url】
                    softRefMap.remove(url);
                }
            }
        }
        return bitmap;
    }

    /**
     * 销毁硬引用中的Bitmap
     * @param key
     */
    public void releaseCacheBitmap(String key) {
        if (key != null && memoryCache.get(key) != null) {
            Bitmap bitmap = memoryCache.remove(key);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    /**
     * 添加图片加载任务，异步执行【线性安全】
     * @param task 图片加载任务【ImageLoadTask】
     */
    public void asyncLoad(ImageLoadTask task) {
        synchronized (taskList) {
            final int findIndex = taskList.indexOf(task);
            //改图片加载任务已经  在  任务列表 存在
            if (findIndex != -1) {
                final ImageLoadTask item = taskList.get(findIndex);
                //当改加载任务处在READY状态
                if (item.getStatus() == ImageLoadTask.STATUS_READY) {
                	//从任务列表移除该任务
                    taskList.remove(findIndex);
                    //重新将该任务添加到  任务列表  尾部
                    taskList.addLast(item);
                }
            } else {
            	//当任务列表为空，发送消息【加载开始】
                if (taskList.size() == 0) {
                    handler.sendEmptyMessage(ACTION_LOAD_START);
                }
                //将该任务添加到  任务列表  尾部
                taskList.addLast(task);
                //执行  TaskWorker
                executorService.execute(new TaskWorker());
            }
        }
    }

    /**
     * 添加延迟任务【线性安全】
     * @param task 加载任务
     */
    public void addDelayTask(ImageLoadTask task) {
        synchronized (taskList) {
            int index = taskList.lastIndexOf(task);
            if (index == -1) {
            	//将任务  添加到  任务列表  的首部【第一位  最后执行】
                taskList.addFirst(task);
                //执行 TaskWorker
                executorService.execute(new TaskWorker());
            }
        }
    }

    /**
     * 清空  硬引用/软引用/任务列表
     */
    public void clear() {
    	//清空硬引用
        memoryCache.evictAll();
        //清空软应用
        softRefMap.clear();
        //清空任务列表
        removeAllTask();
    }

    /**
     * 清空任务列表
     */
    private void removeAllTask() {
        synchronized (taskList) {
            taskList.clear();
        }
    }

    /**
     * 图片加载任务【线程】
     */
    private class TaskWorker implements Runnable {

        private ImageLoadTask seekReadyTask() {
            synchronized (taskList) {
            	int size = taskList.size();
            	int i;
            	//从列表的最后一项开始遍历，当任务处于ready状态，修改其状态为started，并返回该 任务
            	// TODO 【此处算法有待优化】
                for (i = size - 1; i >= 0; i--) {
                    final ImageLoadTask loadTask = taskList.get(i);
                    if (loadTask.getStatus() == ImageLoadTask.STATUS_READY) {
                        loadTask.setStatus(ImageLoadTask.STATUS_STARTED);// 改变其任务状态
                        return loadTask;
                    }
                }
            }
            return null;
        }

        /**
         * 从网络加载图片
         * 
         * @param loadTask
         * @return
         */
        private Bitmap loadFromSource(ImageLoadTask loadTask) {
        	//从网络获取 Bitmap
            Bitmap bitmap = loadTask.doInBackground();
            //取得bitmap成功
            if (bitmap != null) {
            	//bitmap压缩成字节数组
                BytesArray bytesArray = loadTask.transBitmapToBytesArray(bitmap);
                if (bytesArray != null) {
                    // 将图片数据持久化写入磁盘【SDcard】之中
                    DiskCache.writeDiskCache(loadTask.key, bytesArray);
                    //虚拟内存不为空
                    if (memoryFileCache != null) {
                        // 将图片数据写入内存文件中，返回虚拟内存中的标识位
                        CacheBlock block = memoryFileCache.writeData(bytesArray);
                        if (block != null) {
                        	// bitmap写入内存文件成功，将内存块保存起来，便于以后访问。
                            memoryFileMap.put(loadTask.filePath, block);
                        }
                    }
                    //销毁字节数组
                    BytesArrayFactory.getDefaultInstance().releaseBytesArray(bytesArray);
                }
            }
            return bitmap;
        }

        /**
         * 从磁盘加载缓存文件
         * 
         * @param loadTask
         * @return
         */
        private Bitmap loadFromDiskCache(ImageLoadTask loadTask) {
        	//从 SDcard 获取字节数组
            BytesArray bytesArray = DiskCache.readCache(loadTask.key);
            //当SDcard 无该任务图片，返回空
            if (bytesArray == null) {
                return null;
            }
            //将字节数据转化成Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytesArray.getData(), bytesArray.offset(), bytesArray.size());
            //将字节数据保存到虚拟内存
            if (memoryFileCache != null) {
                CacheBlock cacheBlock = memoryFileCache.writeData(bytesArray);
                if (cacheBlock != null) {
                    memoryFileMap.put(loadTask.filePath, cacheBlock);
                }
            }
            //销毁字节数组
            BytesArrayFactory.getDefaultInstance().releaseBytesArray(bytesArray);
            return bitmap;
        }

        /**
         * 从虚拟内存文件中加载缓存Bitmap
         * 
         * @param loadTask
         * @param block
         * @return
         */
        private Bitmap loadFromMemoryCache(ImageLoadTask loadTask, CacheBlock block) {
            Bitmap bitmap = null;
            if (memoryFileCache != null) {
                int size = block.endIndex - block.startIndex;
                BytesArrayFactory factory = BytesArrayFactory.getDefaultInstance();
                BytesArray bytesArray = factory.requestBytesArray(size);
                if (memoryFileCache.readData(block, bytesArray)) {
                    bitmap = BitmapFactory
                            .decodeByteArray(bytesArray.getData(), bytesArray.offset(), bytesArray.size());
                } else {// 缓存被覆盖清除
                    memoryFileMap.remove(loadTask.filePath);
                }
                factory.releaseBytesArray(bytesArray);
            }
            return bitmap;
        }

        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
            /* 设置任务线程为后台线程，获得更少的执行机会，减少对UI流畅度的影响 */
            int threadPriority = Process.getThreadPriority(Process.myTid());
            switch (currentTaskPriority) {
            case TASK_PRIORITY_MAX:
                if (threadPriority != Process.THREAD_PRIORITY_DEFAULT) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                }
                break;
            case TASK_PRIORITY_MIDDLE:
                if (threadPriority != Process.THREAD_PRIORITY_BACKGROUND) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                }
                break;
            case TASK_PRIORITY_MIN:
                if (threadPriority != Process.THREAD_PRIORITY_LOWEST) {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                }
                break;
            }
            ImageLoadTask loadTask = seekReadyTask();
            if (loadTask == null) {
                // handler.sendEmptyMessage(ACTION_LOAD_FINISH);
                return;
            }
            try {
                Bitmap bitmap = null;
                // 查看虚拟内存，看是否已被缓存【标识位】
                CacheBlock block = memoryFileMap.get(loadTask.filePath);
                // 尚未缓存
                if (block == null) {
                	//从磁盘去取
                    bitmap = loadFromDiskCache(loadTask);
                    //银盘尚未缓存
                    if (bitmap == null) {
                    	//从网络去取
                        bitmap = loadFromSource(loadTask);
                    }
                } else {
                	// 已经虚拟内存缓存过，直接取自虚拟内存
                    bitmap = loadFromMemoryCache(loadTask, block);
                }
                // 取得图片成功
                if (bitmap != null) {
                    // 将bitmap放入LruCache缓存列表
                    memoryCache.put(loadTask.filePath, bitmap);
                }
                //发送消息，图片加载成功
                final Message message = handler.obtainMessage(ACTION_LOAD_COMPLETED);
                final Bundle bundle = new Bundle(2);
                bundle.putSerializable(MAP_KEY_TASK, loadTask);
                bundle.putParcelable(MAP_KEY_BITMAP, bitmap);
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (OutOfMemoryError e) {
            	//清空内存中的引用
                memoryCache.evictAll();
                softRefMap.clear();
                BDebug.e(TAG, "Clear Memory!!!!!!!!!!!!!!!!!!!!!:");
                System.gc();
                e.printStackTrace();
                BDebug.e(TAG, "OutOfMemoryError!!!!!!!!!!!!!!!!!!!!:" + e.getMessage());
            } finally {
                if (loadTask != null) {
                    synchronized (taskList) {
                    	//任务列表移除该任务
                        taskList.remove(loadTask);
                    }
                }
            }
        }
    }

    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
            case ACTION_LOAD_COMPLETED:
                final Bundle bundle = msg.getData();
                final ImageLoadTask task = (ImageLoadTask) bundle.getSerializable(MAP_KEY_TASK);
                final Bitmap bitmap = bundle.getParcelable(MAP_KEY_BITMAP);
                //图片加载完成
                task.performCallback(bitmap);
                break;
            case ACTION_LOAD_START:
                break;
            case ACTION_LOAD_FINISH:
                break;
            }
        }

    };

    /**
     * 图片加载状态接口
     */
    public static interface ImageLoadStatusListener {

        void onImageLoadStart(ImageLoaderManager manager);

        void onImageLoadFinish(ImageLoaderManager manager);
    }

}
