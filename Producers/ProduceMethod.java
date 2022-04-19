package Producers;

import java.io.Serializable;
import java.util.ArrayList;

public interface ProduceMethod extends Serializable {
    void execute(ArrayList<Resource> resources, double unionPower);
}
