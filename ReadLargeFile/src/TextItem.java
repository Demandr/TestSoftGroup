/**
 * Created by Oleksandr on 09.03.2017.
 */
public class TextItem {
    private int count;
    private String text;

    public TextItem() {
        this.count = 1;
    }

    public int getCount() {
        return count;
    }

    public void upCount() {
        this.count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
