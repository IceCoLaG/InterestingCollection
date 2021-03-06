package com.ic.learn.concurrent.useCyclicBarrier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
银行流水处理服务类
* @author zdk
* */
public class BankWaterService implements Runnable {
    /*创建四个屏障，处理完之后执行当前类的run方法*/
    private CyclicBarrier c = new CyclicBarrier(4, this);
    /*假设只有4个sheet，所以只启动4个线程*/
    private Executor executor = Executors.newFixedThreadPool(4);
    /*保存每个sheet计算出的银行流水结果*/
    private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

    private void count() {
        for (int i = 0; i < 4; i++){
            executor.execute(()->{
                /*计算当前sheet的银流数据，代码略过*/
                sheetBankWaterCount.put(Thread.currentThread().getName(),1);
                /*银流计算完成，插入一个屏障*/
                try {
                    c.await();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void run() {
        int result = 0;
        for (Map.Entry<String,Integer> sheet : sheetBankWaterCount.entrySet()){
            result += sheet.getValue();
        }

        sheetBankWaterCount.put("result",result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
