import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This solution will iterate through all the lines
 * in the file – allowing for processing of each line – without
 * keeping references to them – and in conclusion, without keeping
 * them in memory: (~150 Mb consumed)
 */
public class app {

    public static void main(String[] args) throws IOException {

        String path = "Text.txt";
        ArrayList<TextItem> items = new ArrayList<>();
        String textAdd = "";

        int index = 0;

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int counter = check(line);

                String[] parts = line.split("\\|");
                parts[0] = textAdd + parts[0];

                if (counter < parts.length){
                    textAdd = parts[parts.length - 1];
                }else {
                    textAdd = "";
                }

                for (int i = 0; i < checkLength(parts.length, textAdd); i++) {
                    TextItem item = new TextItem();
                    item.setText(parts[i]);
                    if (checkItem(items, item.getText()) == -1) {
                        items.add(index, item);
                        index++;
                    }else {
                        items.get(checkItem(items, item.getText())).upCount();
                    }

                }

            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        System.out.println();

        items.remove(0);

       Collections.sort(items, new Comparator<TextItem>() {
            @Override
            public int compare(TextItem o1, TextItem o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        int size;
        if (items.size() > 100000){
            size = 100000;
        }else {
            size = items.size();
        }

        for (int i = 0; i < size; i++){
            System.out.println(items.get(i).getText() + "     " + items.get(i).getCount());
        }
    }

    private static int check (String txt){
        Pattern p = Pattern.compile("\\|");
        Matcher m = p.matcher(txt);
        int counter = 0;
        while(m.find()) {
            counter++;
        }
        return counter;
    }
    private static int checkLength (int count, String txt){
        if(count == 1){
            return 1;
        }if (count > 1 && !(txt.isEmpty())){
            return count - 1;
        }else {
        return count;
        }
    }

    private static int checkItem (ArrayList<TextItem> list, String txt){
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getText().equals(txt)){
                return i;
            }
        }
        return -1;
    }
}
