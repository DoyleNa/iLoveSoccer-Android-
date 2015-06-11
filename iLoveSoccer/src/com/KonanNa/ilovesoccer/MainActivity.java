package com.KonanNa.ilovesoccer;

import net.daum.mf.oauth.MobileOAuthLibrary;
import net.daum.mf.oauth.OAuthError;
import net.daum.mf.oauth.OAuthError.OAuthErrorCodes;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	static final String CLIENT_ID = "6818408208115192452";		//테스트용
	
	private TextView logText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        createOAuth();
        initializeObj();
        
        Uri uri = getIntent().getData();
        if (uri != null) {
            // authorize() 호출 후에 url scheme을 통해 callback이 들어온다.
            MobileOAuthLibrary.getInstance().handleUrlScheme(uri);
        }
    }

    
    private void createOAuth()
    {
    	MobileOAuthLibrary.getInstance().initialize(getApplicationContext(), CLIENT_ID);
    }
    
    private void initializeObj()
    {
    	logText = (TextView)findViewById(R.id.tv_log);
    	
    	Button verify = (Button)findViewById(R.id.verify);
    	verify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileOAuthLibrary.getInstance().authorize(MainActivity.this, oAuthListener);
			}
		});
    	
    	Button profile = (Button)findViewById(R.id.profile);
    	profile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobileOAuthLibrary.getInstance().requestResourceWithPath(getApplicationContext(), oAuthListener, "https://apis.daum.net/cafe/v1/boards/WorldcupLove");
			}    		
    	});
    }
    
    @Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
    	super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            // authorize() 호출 후에 url scheme을 통해 callback이 들어온다.
            MobileOAuthLibrary.getInstance().handleUrlScheme(uri);
        }

	}

	private MobileOAuthLibrary.OAuthListener oAuthListener = new MobileOAuthLibrary.OAuthListener() {		
		@Override
		public void onRequestResourceSuccess(String arg0) {
			// TODO Auto-generated method stub
			Log.d("test", "Resource:"+arg0);
			logText.append("리소스요청성공:"+arg0);
		}
		
		@Override
		public void onRequestResourceFail(OAuthErrorCodes errorCode, String strErrorMsg) {
			// TODO Auto-generated method stub
			logText.append("리소스요청실패:"+strErrorMsg);
			if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorInvalidToken))
			{
	            // access token 이 없거나 만료처리된 경우 or 401 에러             // authorize() 를 통해 다시 access token을 발급 받아야함.
	        } 
			else if   (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorInvalidResourceRequest))
			{
	            // 서버와 통신중 400 에러가 발생한 경우         
	        } 
			else if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorInsufficientScope)) 
	        {
	            // 서버와 통신중 403 에러가 발생한 경우        
	        } 
	        else if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorServiceNotFound)) 
	        {
	            // 서버와 통신중 404 에러가 발생한 경우         
	        } 
	        else if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorNetwork)) 
	        {
	            // 현재 휴대폰의 네트워크를 이용할 수 없는 경우        
	        } 
	        else if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorServer)) 
	        {
	            // 서버쪽에서 에러가 발생하는 경우             // 서버 페이지에 문제가 있는 경우이므로 api 담당자와 얘기해야함.
	        } 
	        else if (errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorUnknown)) 
	        {
	            // 서버와 통신중 그 외 알수 없는 에러가 발생한 경우.
	        }

		}
		
		@Override
		public void onAuthorizeSuccess() {
			// TODO Auto-generated method stub
			logText.append("인증 성공!");
		}
		
		@Override
		public void onAuthorizeFail(OAuthErrorCodes errorCode, String strErrorMsg) {
			// TODO Auto-generated method stub
			logText.append("인증실패:"+strErrorMsg);
			if(errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorInvalidResourceRequest))
			{
				//파라미터 잘못사용
			}
			else if(errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorUnknown))
			{
				//승인되지 않은 Client ID사용
			}
			else if(errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorAccessDenied))
			{
				//사용자가 승인 페이지에서 "취소"를 누른경우
			}
			else if(errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorUnsupportedResponseType))
			{
				//지원되지 않는 인증방식을 사용한 경우
			}
			else if(errorCode.equals(OAuthError.OAuthErrorCodes.OAuthErrorInvalidScope))
			{
				//유효한 권한 요청이 아닌경우
			}
		}
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 사용이 끝나면 반드시 호출해주어야 한다.
        MobileOAuthLibrary.getInstance().uninitialize();
    }
}
