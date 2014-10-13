package seu.url;

public class LogUrlManager {
	
	private static final LogUrlManager urlManager = new LogUrlManager();
	
	private LogUrlManager() {
		
	}
	
	/**
	 * static factory method
	 * @return A new instance of LogUrlManager
	 */
	public static LogUrlManager GetInstance(){
		return urlManager;
	}
	
	/**
	 * ��һ�ι��ˣ����˵õ�ԭʼ�� request, referer
	 * @param input		mr.data.txt	-	MapReduce������ݼ�<request_hostName, dateTime, request, referer>
	 * @param output	1st.txt		-	�淶��url��ȥ�����е���Ч��Դ�ļ�¼
	 */
	public void FilterRequestAndResponse(String input, String output){
		LogFilter.getRequestAndReferer(input, output);
	}
	
	
	/**
	 * �ڶ��ι��ˣ����˳�������վ�����վ�ڵ�����
	 * @param input		1st.txt
	 * @param output	2nd.txt	-	���˵õ�����վ�����վ�ڵĽڵ�
	 */
	public void FilterOutsiteRequest(String input, String output){
		LogFilter.getOutsiteRequests(input, output);
	}
	
	/**
	 * �����Σ���ϴ���е���վurl���������е��������
	 * @param input		2nd.txt
	 * @param output	3rd.txt	-	���˵�������վurl�������������ȡ��վurl��������ַ
	 * 			<request_host, request, referer_host>
	 */
	public void CleanOutsiteRequest(String input, String output){
		LogFilter.cleanOutsiteRequests(input, output);
	}
	
	/**
	 * 
	 * @param input		3rd.txt
	 * @param output	4th.txt - ��������վ��host��������ҳ���host
     *        (request_host, referer_host)
	 */
	public void getHostOfRequestAndReferer(String input, String output){
		LogFilter.getRequestAndRefererHost(input, output);
	}

    /**
     *
     * @param input     4th_count.txt   (request_host, referer_host, dup_count)
     * @param output    5th.txt         (referer_host, dup_count, referer_domain)
     *        then      (request_host, referer_host, dup_count, referer_domain) -> JSON file
     */
    public void getOutsiteHostDistribution(String input, String output){
        LogFilter.getOutsiteHostDistribution(input, output);
    }
}
