package com.hogee.bootprof;

public class BootSubService {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        BootSubService service = new BootSubService();
        service.praseBoot();
    }
    
    
    private String mBootProfPath = "src/com/hogee/bootprof/pixi35user/bootprof";// Attention: MTK have this feature, but not QCOM
    
    public void praseBoot() {
        BootAnalyzer analyzer = new BootAnalyzer();
        analyzer.matches(mBootProfPath);
    }
    

}
