import java.util.ArrayList;

public class TermoSort {
    ArrayList<Integer> numbers;
    public TermoSort() {
        numbers = new ArrayList<Integer>();
    }
    private Integer compare(int num,int num1) {
        float delta = (float) (num - num1)/numbers.size();
        return null;
    }

    public void addNum(int num){
        numbers.add(num);
    }

    public void removeLastNum() {
        numbers.remove(numbers.size()-1 );
    }

    public void sort(int num) {

    }
}
