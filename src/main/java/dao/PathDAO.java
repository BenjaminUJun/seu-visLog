package dao;

import com.mongodb.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Minglu SHAO
 */
public class PathDAO {

    private DBCollection path;

    public PathDAO(DB db) { path = db.getCollection("path"); }

    /** ��Ҫ����ʱ�����
     *  ����һ�η���
     * @param step
     * @param nextStep
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public List<DBObject>  groupByFour(int  step, int nextStep,String startTime, String endTime) throws ParseException {
        String start="P"+step;
        String end ="P"+nextStep;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //matchɸѡ,ʱ��Ҫ���
        //start & end ����"null"
        BasicDBObject[] arrayCond = {
                new BasicDBObject("time", new BasicDBObject("$gte", sdf.parse(startTime))),
                new BasicDBObject("time", new BasicDBObject("$lt", sdf.parse(endTime))),
                new BasicDBObject(start, new BasicDBObject("$ne", "null")),
                new BasicDBObject(end, new BasicDBObject("$ne", "null")),
        };
        BasicDBObject cond = new BasicDBObject();
        cond.put("$and", arrayCond);
        DBObject match = new BasicDBObject("$match", cond);

        //����$projectƴװgroup��Ҫ������
        DBObject fields = new BasicDBObject(start, 1);
        fields.put(end, 1);
        fields.put("session", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject _group = new BasicDBObject(start, "$" + start);
        _group.put(end, "$" + end);
        _group.put("session", "$session");
        //_group��Ϊgroup������
        DBObject groupFields = new BasicDBObject("_id", _group);
        groupFields.put("nums", new BasicDBObject("$sum", 1));  //��Ϊ1
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_group", 1));
        //run
        DBObject out=new BasicDBObject("$out","tmp_out");
        List<DBObject> pipeline = Arrays.asList(match, project, group, sort, out);

        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).batchSize(10000).build();

        Cursor cursor = path.aggregate(pipeline, options);
        List<DBObject> pathGroupList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            pathGroupList.add(item);
        }
        return pathGroupList;
    }

    /**��Ҫ����ʱ�����
     * ���P1��URL���ڼ������
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public List<DBObject> getDepth0(String startTime,String endTime)throws ParseException{
        String start="P1";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //matchɸѡ,ʱ��Ҫ���
        //start ����"null"
        BasicDBObject[] arrayCond = {
                new BasicDBObject("time", new BasicDBObject("$gte", sdf.parse(startTime))),
                new BasicDBObject("time", new BasicDBObject("$lt", sdf.parse(endTime))),
                new BasicDBObject(start, new BasicDBObject("$ne", "null")),
        };
        BasicDBObject cond = new BasicDBObject();
        cond.put("$and", arrayCond);
        DBObject match = new BasicDBObject("$match", cond);

        //����$projectƴװgroup��Ҫ������
        DBObject fields = new BasicDBObject(start, 1);
        fields.put("session", 1);
        DBObject project = new BasicDBObject("$project", fields);
        // $group
        DBObject _group = new BasicDBObject(start, "$" + start);
        _group.put("session", "$session");
        //_group��Ϊgroup������
        DBObject groupFields = new BasicDBObject("_id", _group);
        groupFields.put("nums", new BasicDBObject("$sum", 1));  //��Ϊ1
        DBObject group = new BasicDBObject("$group", groupFields);
        // $sort
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_group", 1));
        //run
        DBObject out=new BasicDBObject("$out","tmp_out");
        List<DBObject> pipeline = Arrays.asList(match, project, group, sort,out);
        //allowDiskUse
        AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).batchSize(10000).build();
        Cursor cursor = path.aggregate(pipeline, options);
        List<DBObject> pathGroupList = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            pathGroupList.add(item);
        }
        return pathGroupList;
    }
}
