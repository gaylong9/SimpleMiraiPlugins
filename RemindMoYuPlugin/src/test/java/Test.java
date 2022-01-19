import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {

//        System.out.println(Arrays.toString("123".split("\\[time]")));
//        System.out.println(Arrays.toString("[time]3[time]".split("\\[time]")));

//        String time = "\\[time]";
//        String face = "\\[face:.*?]";
//        String faceIdRegex = "(?<=\\[face:).*?(?=])";
//        List<Integer> timeStartIdxs = new LinkedList<>();
//        List<Integer> faceStartIdxs = new LinkedList<>();
//        List<Integer> faceEndIdxs = new LinkedList<>();
//
//        String content = "hi[face:1]hi[time]hi[face:2]hi[time]hi[face:3]hi[face:4]";
//
//        Pattern facePattern = Pattern.compile(face);
//        Matcher matcher = facePattern.matcher(content);
//        while (matcher.find()) {
//            faceStartIdxs.add(matcher.start(0));
//            faceEndIdxs.add(matcher.end(0));
//        }
//
//        Pattern timePattern = Pattern.compile(time);
//        matcher = timePattern.matcher(content);
//        while (matcher.find()) {
//            timeStartIdxs.add(matcher.start(0));
//        }
//        faceStartIdxs.add(content.length());
//        timeStartIdxs.add(content.length());
//        MessageChainBuilder builder = new MessageChainBuilder();
//        for (int curIdx = 0; curIdx < content.length(); ) {
//            if (curIdx == faceStartIdxs.get(0)) {
//                builder.append(content.substring(faceStartIdxs.get(0), faceEndIdxs.get(0)));
//                curIdx = faceEndIdxs.get(0);
//                faceStartIdxs.remove(0);
//                faceEndIdxs.remove(0);
//            } else if (curIdx == timeStartIdxs.get(0)) {
//                builder.append(new MyTime());
//                timeStartIdxs.remove(0);
//                curIdx += 6;
//            } else {
//                int toIdx = Math.min(faceStartIdxs.get(0), timeStartIdxs.get(0));
//                builder.append(content.substring(curIdx, toIdx));
//                curIdx = toIdx;
//            }
//        }
//
//        System.out.println(builder.build());
//        return;


//        System.out.println(Arrays.toString(content.split(face)));


//        String[] timeSplits = content.split(time);
//        for (int i = 0; i < timeSplits.length; i++) {
//            String[] splits = timeSplits[i].split(face);
//            for (int j = 0; j < splits.length; j++) {
//                String split = splits[j];
//                builder.append(split);
//                if (j < splits.length - 1) {
//                    builder.append("[face:").append(String.valueOf(ids.get(idx++))).append("]");
//                }
//            }
//            if (idx < ids.size() && timeSplits[i].endsWith("[face:"+ ids.get(idx) +"]")) {
//                builder.append("[face:").append(String.valueOf(ids.get(idx++))).append("]");
//            }
//            if (i < timeSplits.length - 1) {
//                builder.append(new MyTime());
//            }
//        }
//        if (content.endsWith("[time]")) {
//            builder.append(new MyTime());
//        }
//        System.out.print(builder.build());

//        MyTime time = new MyTime();
//        MessageChainBuilder builder = new MessageChainBuilder();
//        builder.append("[");
////        builder.append(time);
//        builder.append("]");


//        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
//        Runnable task = () -> {
//            System.out.println(builder.build());
//        };
//
//        executor.scheduleAtFixedRate(task, 1, 5, TimeUnit.SECONDS);


//        List<LocalTime> times = new ArrayList<>();
//        times.add(LocalTime.of(10, 0));
//        times.add(LocalTime.of(11, 20));
//
//        Yaml yaml = new Yaml();
//        String ymlPath = "src/test/java/test.yml";
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ymlPath, false))) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("times", times);
//            yaml.dump(map, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(ymlPath))) {
//            Map<String, Object> content = yaml.load(reader);
//            List<LocalTime> gotTimes = new ArrayList<>();
//            List<?> rawTimes = (ArrayList<?>) content.get("times");
//            for (Object rawTime : rawTimes) {
//                gotTimes.add((LocalTime) rawTime);
//            }
//            System.out.println(gotTimes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String regex = "\\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("\n");
        while (matcher.find()) {
            System.out.println(matcher.start(0));
        }
    }
}
