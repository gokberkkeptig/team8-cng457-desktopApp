package sorters;

import jsonhandlers.Phone;

import java.util.Comparator;

public class PhoneSorter implements Comparator<Phone> {
    @Override
    public int compare(Phone o1, Phone o2) {
        return o2.getPrice().compareTo(o1.getPrice());
    }
}
