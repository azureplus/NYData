import com.alibaba.fastjson.JSON;
import com.itown.kas.pfsc.report.po.HqPara;
import flex.messaging.io.ArrayCollection;
import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;
import flex.messaging.io.amf.client.exceptions.ServerStatusException;

/**
 * Created by dev on 15/12/14.
 */
public class Main
{

   static  String amfConnectUrl = "http://jgsb.agri.gov.cn/messagebroker/amf";
    static AMFConnection amfConnection = new AMFConnection();
    public static void main(String[] args)
    {
        try
        {
            amfConnection.connect(amfConnectUrl);
            ArrayCollection result = (ArrayCollection) amfConnection.call("reportStatService.getHqInitData");
            handleData((ArrayCollection)result.get(result.size()-1));
        } catch (ServerStatusException | ClientStatusException e)
        {
            e.printStackTrace();
        }

    }

    private static  HqPara hqPara = new HqPara();

    private static void handleData(ArrayCollection nyData) throws ClientStatusException,ServerStatusException
    {
        ArrayCollection pageData = (ArrayCollection)nyData.get(0);
        int index  = Integer.parseInt(nyData.get(1).toString());
        int pageTotal = Integer.parseInt(nyData.get(2).toString());
        int itemTotal = Integer.parseInt(nyData.get(3).toString());

        System.out.println("\n获得第 " + index + " 页数据");
        ArrayCollection result;
        for (int i = 0; i < pageData.size(); i++)
        {
            Object json = JSON.toJSON(pageData.get(0));
            System.out.println(json);
        }
        if (pageTotal >= index)
        {
            result = (ArrayCollection) amfConnection.call("reportStatService.getHqSearchData",hqPara, (index + 1 )+"", 15 + "");
            if(result!=null)
            {
                handleData(result);
            }
        }

    }
}
