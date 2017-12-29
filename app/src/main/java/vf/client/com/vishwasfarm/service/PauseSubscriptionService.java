package vf.client.com.vishwasfarm.service;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import vf.client.com.vishwasfarm.ServiceListener.OnDeleteSubscription;
import vf.client.com.vishwasfarm.fragments.SubscriptionFragment;
import vf.client.com.vishwasfarm.utility.VishwasServices;

public class PauseSubscriptionService extends AsyncTask<Void, Integer, String> implements VishwasServices{

	private SubscriptionFragment mSubscriptionFragment;
	private String mSubCode;
	String mCustCode;
	int status ;
	OnDeleteSubscription onDeleteSubscription;
	private String mChgStartDate;
	private String mChgEndDate;

	public PauseSubscriptionService(SubscriptionFragment fFragment, String CustCode, String fsubID, String fChgStartDate, String fChgEndDate) {


		mSubscriptionFragment =fFragment;
		onDeleteSubscription = (OnDeleteSubscription) mSubscriptionFragment;
		mCustCode=CustCode;
		mSubCode=fsubID;
		mChgStartDate=fChgStartDate;
		mChgEndDate=fChgEndDate;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			HttpURLConnection lSendMsgConnection = null;


			String URL=mDeleteSubscription;

			URL+="?cod_cust="+mCustCode+"&subs_id="+mSubCode+"chg_start_date"+mChgStartDate+"chg_end_date"+mChgEndDate;

			String formatedURL=URL.replaceAll(" ", "%20");

			URL lPriceURL = new URL(formatedURL);


			lSendMsgConnection = (HttpURLConnection) lPriceURL.openConnection();
			lSendMsgConnection.setRequestMethod("GET");
			//lSendMsgConnection.setDoOutput(false);

//	            lSendMsgConnection.setRequestProperty("Accept","*/*");

			lSendMsgConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			lSendMsgConnection.setRequestProperty("Content-Language", "en-US");
			lSendMsgConnection.setRequestProperty("Proxy-Connection", "keep-alive");


			status = lSendMsgConnection.getResponseCode();
			InputStream lPriceInputStream=null;
			if(status!=200){
				lPriceInputStream=lSendMsgConnection.getErrorStream();
			}
			else{
				lPriceInputStream = lSendMsgConnection.getInputStream();
			}


			BufferedReader reader = new BufferedReader(new InputStreamReader(lPriceInputStream));
			StringBuilder result = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result.toString());
			lSendMsgConnection.disconnect();
			return result.toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		if(status==200){
			onDeleteSubscription.onDeleteSubsciptionResult(true,result);

			Toast.makeText(mSubscriptionFragment.getActivity(),"Registration done Successfully", Toast.LENGTH_SHORT).show();
		}
		else{
			onDeleteSubscription.onDeleteSubsciptionResult(false,result);
			Toast.makeText(mSubscriptionFragment.getActivity(),"Unable to sregister user, Please try again later..", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

}
