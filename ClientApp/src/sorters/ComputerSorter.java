package sorters;

import jsonhandlers.Computer;
import jsonhandlers.Phone;

import java.util.Comparator;

public class ComputerSorter implements Comparator<Computer> {
    @Override
    public int compare(Computer o1, Computer o2) {
        return o2.getPrice().compareTo(o1.getPrice());
    }
}
