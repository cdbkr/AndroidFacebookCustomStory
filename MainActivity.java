import java.util.ArrayList;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button button;
	private UiLifecycleHelper uiHelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		button  = (Button)findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				post();
			}
		});
	}
	
	Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState sessionState, Exception exception) {
			Log.e(this.getClass().toString(), String.format("SessionState: %s", sessionState.isOpened()));
		}
	};

	private void post() {
		OpenGraphObject object = OpenGraphObject.Factory.createForPost("namespaceapp:object");
		object.setProperty("title", "Buffalo Tacos");
		object.setProperty("url", "https://example.com/cooking-app/meal/Buffalo-Tacos.html");
		object.setProperty("description", "Leaner than beef and great flavor.");

		OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
		action.setProperty("object", object);

	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = false;
	    Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, options);

		List<Bitmap> images = new ArrayList<Bitmap>();
		images.add(bitmap);

		FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "namespaceapp:action", "object")
		        .setImageAttachmentsForAction(images, true)
		        .build();

		uiHelper.trackPendingDialogCall(shareDialog.present());

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
}
