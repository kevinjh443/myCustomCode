package elsetest;

import java.util.ArrayList;

public interface MyStrategy {

    public <E> E loadResult(ArrayList<String> fileList);
    public <E> E loadResult2(ArrayList<String> fileList);
    public <E> E loadResult3(String fileList);
}
