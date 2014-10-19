package seu.util;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Weiwei on 10/19/2014.
 */
public class WordCount {

    private static WordCount instance = new WordCount();

    private WordCount(){}

    public static WordCount getInstance(){
        if(instance!=null)
            return instance;
        else
            return new WordCount();
    }

    /**
     *
     * @param input     �����ļ�Ϊ��ÿ��һ�����ʻ�һ������
     * @param output    ����ļ�ΪJavaScript���б���ʽ����[['word1', 4],['word2',5]]����ʽ
     * @throws IOException
     */
    public void CountWordFromFile(String input, String output) throws IOException{

        try(
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        ){
            String line = null;

            //<word, count>
            Map<String, Integer> wordMap = new HashMap<>();

            while((line=br.readLine())!=null){
                line.replaceAll("\\+"," ");
                if(wordMap.get(line)==null){
                    wordMap.put(line,1);
                }else{
                    int count = wordMap.get(line);
                    wordMap.put(line, ++count);
                }
            }

            Iterator iter = wordMap.keySet().iterator();
            String newLine;

            while(iter.hasNext()) {
                String key = String.valueOf(iter.next());
                Object val = wordMap.get(key);
                newLine = "['"+key + "'," + val+"],";

                if(newLine!=null){
                    bw.write(newLine);
                    bw.newLine();
                }
            }


        }
    }
}
