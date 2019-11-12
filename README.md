# demo2
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping(path = { "/HelloSpringBoot" })
    public String getMethod() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        long start = System.currentTimeMillis();
        Future<Persion> submit = taskExecutor.submit(new PersionThread(countDownLatch));
        Future<Persion> submit2 = taskExecutor.submit(new PersionThread2(countDownLatch));
        Future<Persion> submit3 = taskExecutor.submit(new PersionThread3(countDownLatch));
        Persion persion = submit.get(59000, TimeUnit.MILLISECONDS);
        Persion persion2 = submit2.get(59000, TimeUnit.MILLISECONDS);
        Persion persion3 = submit3.get(50000, TimeUnit.MILLISECONDS);
        countDownLatch.await(10000,TimeUnit.MILLISECONDS);
        System.out.println("end.................................");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return persion2.getCar();

    }
    
    
    #hhh
    ublic class PersionThread3 implements  Callable<Persion>{

    private  CountDownLatch  countDownLatch;

    @Override
    public Persion call() throws Exception {
        Thread.sleep(15000);
        System.out.println("persion33");
        Persion p  =new Persion("33", "zz");
        countDownLatch.countDown();
        return p;
    }

    public PersionThread3(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

#kkkkkkkkkkkkkkk

@Configuration
public class ExecutorSource {

    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
	public ThreadPoolTaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);   
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(200);
		executor.setKeepAliveSeconds(10);
		executor.setThreadNamePrefix("risk-service");		
		// 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 初始化
		executor.initialize();
		return executor;
	}   
}
