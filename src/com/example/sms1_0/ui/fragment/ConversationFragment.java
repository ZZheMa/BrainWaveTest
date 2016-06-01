package com.example.sms1_0.ui.fragment;

 
 
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brainwave_test.R;
import com.example.sms1_0.base.BaseFragment;
import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

public class ConversationFragment extends BaseFragment {

	private BluetoothAdapter bluetoothAdapter;
	private TGDevice tgDevice;
	private Button bt_connect;
	private TextView tv_state;
	private ScrollView sv_state;
	private int subjectContactQuality_last;
	private int subjectContactQuality_cnt;
	private boolean connected = false;
	private boolean started = false;
	public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		this.bluetoothAdapter = bluetoothAdapter;
	}
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_conversation, null);
		bt_connect = (Button)view.findViewById(R.id.bt_connect);
		tv_state = (TextView)view.findViewById(R.id.tv_state);
		sv_state = (ScrollView)view.findViewById(R.id.sv_state);
		return view;
	}

	@Override
	public void initData() {
		subjectContactQuality_last = -1; /* start with impossible value */
	    subjectContactQuality_cnt = 200; /* start over the limit, so it gets reported the 1st time */

		//��������
		bt_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!connected && tgDevice != null && tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
					tgDevice.connect(true);
					bt_connect.setText("Stop");
				} else {
					if (started) {
						tgDevice.stop();
						started = false;
						bt_connect.setText("Start");
					} else {
						tgDevice.start();
						started = true;
						bt_connect.setText("Stop");
					}
				}
			}
		});
		
		//��������������Ƿ�����
		if( bluetoothAdapter == null ) {            
		    // Alert user that Bluetooth is not available
			Toast.makeText( getActivity(), "Bluetooth not available", Toast.LENGTH_LONG ).show();
			return;
		} else {
			//���������������������ô����tgDevice����	
			tgDevice = new TGDevice(bluetoothAdapter, connectHandler);
		} 
	}
	
	 /**
     * ��������TGDevice�������Ϣ
     */
     final Handler connectHandler = new Handler() {
    	@Override
        public void handleMessage( Message msg ) {
            switch( msg.what ) {
            	case TGDevice.MSG_MODEL_IDENTIFIED:
                    tgDevice.setBlinkDetectionEnabled(true);
                    tgDevice.setTaskDifficultyRunContinuous(true);
                    tgDevice.setTaskDifficultyEnable(true);
                    tgDevice.setTaskFamiliarityRunContinuous(true);
                    tgDevice.setTaskFamiliarityEnable(true);
                    tgDevice.setRespirationRateEnable(true);  
                    tv_state.append("���ܳ�ʼ�Ѿ���ʼ���ɹ�\n");
            		break;
            	
            	// ��������״̬
                case TGDevice.MSG_STATE_CHANGE:
                    switch( msg.arg1 ) {
    	                case TGDevice.STATE_IDLE:
    	                    break;
    	                case TGDevice.STATE_CONNECTING:       	
    	                	tv_state.append( "��������...\n" );
    	                	break;	
                        case TGDevice.STATE_CONNECTED:
                            tv_state.append( "�Ѿ�����.\n" );
                            connected = true;
                            tgDevice.start();
                            started = true;
                            break;
    	                case TGDevice.STATE_NOT_FOUND:
    	                	tv_state.append( "���������κ������豸������������.\n" );
    	                	break;
                        case TGDevice.STATE_ERR_NO_DEVICE:
                            tv_state.append( "�����豸û����ԣ�����������.\n" );
                            break;
    	                case TGDevice.STATE_ERR_BT_OFF:
    	                    tv_state.append( "�����豸û�д򿪣�����������." );
    	                    break;
    	                case TGDevice.STATE_DISCONNECTED:
    	                	tv_state.append( "�Ͽ�����.\n" );
                    } 
                    break;
    
                 // ���ź�
                 // ��������Ӧ������оƬ�������ź�ǿ����,��ֵͨ����ThinkGearӲ���豸ÿ�����һ��.
                 // ֵΪ0��ʾ���ﴫ�������ܵ����źźܺ�û���κ��������⡣��1~199�еõ�Խ�ߵ�ֵ��ʾ���ﴫ������⵽�ź������Խ�ࡣ
                 // ֵ200��ʾ��������������û�нӴ���������.
                case TGDevice.MSG_POOR_SIGNAL:
                	/* ÿ��ʮ����ʾһ���ź�ǿ�ȣ�����ϲ�ź�ǿ�ȸı�ʱ����ʾ�ź�ǿ��*/
                	if (subjectContactQuality_cnt >= 30 || msg.arg1 != subjectContactQuality_last) {
                		if (msg.arg1 == 0) {
                			tv_state.append( "SignalQuality: is Good: " + msg.arg1 + "\n" );
                		} else {
                			tv_state.append( "SignalQuality: is POOR: " + msg.arg1 + "\n" );
                		}
                		subjectContactQuality_cnt = 0;
                		subjectContactQuality_last = msg.arg1;
                		
                	} else {
                		subjectContactQuality_cnt++;
                	}	
                	g_Noise = msg.arg1;
                    break;
                
                // �������ԭʼraw����
                case TGDevice.MSG_RAW_DATA:	  
                	break;

                // ע�����ź�
                case TGDevice.MSG_ATTENTION:
                    tv_state.append( "Attention: " + msg.arg1 + "\n" );
                	g_Attention = msg.arg1;
                	 if (g_Attention >= 50) {
 						g_Attention_Control += 5;
 					} else {
 						g_Attention_Control -= 5;
 					}
                     if (g_Attention_Control >= 100) {
                    	 g_Attention_Control = 100;
                     }
                     if (g_Attention_Control <= 0) {
                    	 g_Attention_Control = 0;
 					}
                    break;
                
                // ڤ���ź�
                case TGDevice.MSG_MEDITATION:
                    tv_state.append( "Meditation: " + msg.arg1 + "\n" );
                    g_Meditation = msg.arg1;
                    if (g_Meditation >= 50) {
						g_Meditation_Control += 5;
					} else {
						g_Meditation_Control -= 5;
					}
                    if (g_Meditation_Control >= 100) {
                    	g_Meditation_Control = 100;
                    }
                    if (g_Meditation_Control <= 0) {
						g_Meditation_Control = 0;
					}
                    
                	break;
                	
                // EEG�˸��ź�ֵ
	            case TGDevice.MSG_EEG_POWER:
	            	TGEegPower e = (TGEegPower)msg.obj;
	            //	tv_state.append("delta: " + e.delta + " theta: " + e.theta + " lowAlpha : " + e.lowAlpha + 
	            //			  " highAlpha : " + e.highAlpha + "\nlowBeta : " + e.lowBeta+ " highBeta : " +
	            	//		  e.highBeta + " lowGamma : " + e.lowGamma + " midGamma : " + e.midGamma + "\n");
	            	g_EegPower = (TGEegPower)msg.obj;
	            	if (g_Is_Control_Handler_Ready) {
	            		g_Control_Handler.sendEmptyMessage(1);
					}
	            	if (g_Is_Acquire_Handler_Ready) {
						g_Acquire_Handler.sendEmptyMessage(1);
					}
	            	break;
                
	            // գ���ź�
                case TGDevice.MSG_BLINK:
                	tv_state.append( "Blink: " + msg.arg1 + "\n" );
                	break;
    
                default:
                	break;               	
        	}
        	sv_state.fullScroll( View.FOCUS_DOWN );
        } 
        
     };
}
