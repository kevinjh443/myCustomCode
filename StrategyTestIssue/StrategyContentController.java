package elsetest;

import java.util.ArrayList;

public class StrategyContentController {
    
    private MyStrategy myStrategy;
    
    public StrategyContentController(MyStrategy myStrategy) {
        this.myStrategy = myStrategy;
    }
    
    
    public void getTestResult(ArrayList<String> fileName) {
        this.myStrategy.loadResult(fileName);
        this.myStrategy.loadResult2(fileName);
        this.myStrategy.loadResult3("sdfsdfssd");
    }

}
