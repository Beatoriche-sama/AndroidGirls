package Producers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Function;

public class AndroidUnion  implements Serializable {
    private final ArrayList<Android> androids;

    public AndroidUnion() {
        this.androids = new ArrayList<>();
    }

    public int getPower(Function<Android, Integer> function) {//Android.Stats stats

        return androids.stream()
                .mapToInt(function::apply).sum();//stats //android.getStatValue(skill)
    }

    public void addMember(Android android){
        androids.add(android);
    }

    public void removeMember(Android android){
        androids.remove(android);
    }

    public ArrayList<Android> getAndroids() {
        return androids;
    }
}
