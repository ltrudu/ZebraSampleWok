package com.symbol.lockpinenabler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.admin.DevicePolicyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE = 1234;

    public static MainActivity mActivity;

    DevicePolicyManager mDPM;
    ComponentName mAdminName;

    private TextView mTextViewPolicy;
    private EditText mEditTextPinCode;
    private Button mButtonSetPinCode;
    private Button mButtonSetPolicyFromPin;
    private Button mButtonLockNow;
    private TextView mStatusTextView;
    private Button mButtonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lpe_activity_main);

        mActivity = this;
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, DevAdminTestReceiver.class);

        mTextViewPolicy = (TextView)findViewById(R.id.textView_policy);
        mEditTextPinCode = (EditText)findViewById(R.id.editText_pincode);
        mButtonSetPinCode = (Button)findViewById(R.id.button_setpincode);
        mStatusTextView = (TextView)findViewById(R.id.txt_status);
        mButtonSetPolicyFromPin = (Button)findViewById(R.id.button_setpolicy);
        mButtonLockNow = (Button)findViewById(R.id.button_locknow);
        mButtonResetPassword = (Button)findViewById(R.id.button_resetpassword);

        if(enableAdmin())
            initAll();
    }

    public void initAll()
    {
        mButtonSetPinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set password quality from TextPinCode
                setPasswordQuality();
                // Set pincode
                setPinCode();
            }
        });

        mButtonSetPolicyFromPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordQuality();
            }
        });

        mButtonLockNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDPM.lockNow();
            }
        });

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLockScreen();
            }
        });

        updatePolicyTextView();
    }

    public void resetLockScreen()
    {
        // First disable password quality settings
        mDPM.setPasswordQuality(mAdminName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);

        boolean result = mDPM.getPasswordQuality(mAdminName) == DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;
        if(result)
        {
            mStatusTextView.setText("Set password quality success.");
        }
        else
        {
            mStatusTextView.setText("Set password quality failed.");
        }

        // Then kill the password
        result =  mDPM.resetPassword("", 0);
        if(result)
        {
            mStatusTextView.setText("Reset lock screen success.");
        }
        else
        {
            mStatusTextView.setText("Reset lock screen failed.");
        }

        updatePolicyTextView();
    }

    public void setPinCode()
    {
        String pinCode = mEditTextPinCode.getText().toString();
        if(pinCode.length() > 0)
        {
            // use RESET_PASSWORD_REQUIRE_ENTRY if you want to don't allow other admins to change
            // the password again until the user has entered it, according to the documentation
            // Wasn't able to get this behavior on Kitkat :(
            //boolean result = mDPM.resetPassword(pinCode, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);

            boolean result = mDPM.resetPassword(pinCode, 0);
            if(result)
            {
                mStatusTextView.setText("Set password success.");
            }
            else
            {
                mStatusTextView.setText("Set password failed.");
            }
        }
        else
        {
            mStatusTextView.setText("Error: No pin entered");
        }
    }

    /**
     * This method will return the password quality as a string text
     */
    public String getPassWordQuality()
    {
        int passwordQuality = mDPM.getPasswordQuality(mAdminName);
        switch(passwordQuality)
        {
            case DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC:
                return "PASSWORD_QUALITY_ALPHABETIC";
            case DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC:
                return "PASSWORD_QUALITY_ALPHANUMERIC";
            case DevicePolicyManager.PASSWORD_QUALITY_BIOMETRIC_WEAK:
                return "PASSWORD_QUALITY_BIOMETRIC_WEAK";
            case DevicePolicyManager.PASSWORD_QUALITY_COMPLEX:
                return "PASSWORD_QUALITY_COMPLEX";
            case DevicePolicyManager.PASSWORD_QUALITY_NUMERIC:
                return "PASSWORD_QUALITY_NUMERIC";
            case DevicePolicyManager.PASSWORD_QUALITY_NUMERIC_COMPLEX:
                return "PASSWORD_QUALITY_NUMERIC_COMPLEX";
            case DevicePolicyManager.PASSWORD_QUALITY_SOMETHING:
                return "PASSWORD_QUALITY_SOMETHING";
            case DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED:
                return "PASSWORD_QUALITY_UNSPECIFIED";
        }
        return "PASSWORD_QUALITY_UNSPECIFIED";
    }

    public void updatePolicyTextView()
    {
        String policy = "";
        policy += "Quality:" + getPassWordQuality() + "\n";
        policy += "Min size:" + mDPM.getPasswordMinimumLength(mAdminName) + "\n";
        policy += "Max size:" + mDPM.getPasswordMaximumLength(mDPM.getPasswordQuality(mAdminName)) + "\n";
        policy += "Min letters:" + mDPM.getPasswordMinimumLetters(mAdminName) + "\n";
        policy += "Min lowerCase:" + mDPM.getPasswordMinimumLowerCase(mAdminName) + "\n";
        policy += "Min upperCase:" + mDPM.getPasswordMinimumUpperCase(mAdminName) + "\n";
        policy += "Min non letters:" + mDPM.getPasswordMinimumNonLetter(mAdminName) + "\n";
        policy += "Min symbols:" + mDPM.getPasswordMinimumSymbols(mAdminName) + "\n";
        policy += "Min numeric:" + mDPM.getPasswordMinimumNumeric(mAdminName);

        mTextViewPolicy.setText(policy);
    }

    private void setPasswordQuality()
    {
        String pinCode = mEditTextPinCode.getText().toString();
        if(pinCode.length() > 0)
        {
            setPasswordQualityFromString(pinCode);
            updatePolicyTextView();
        }
    }

    /**
     * Compute then set the password quality from the given password string.
     */
     public void setPasswordQualityFromString(String password) {
         boolean hasDigit = false;
         boolean hasNonDigit = false;
         boolean hasAlphabetic = false;

         final int len = password.length();

         if(len > 0)
         {
             for (int i = 0; i < len; i++) {
                 if (Character.isDigit(password.charAt(i))) {
                     hasDigit = true;
                 }
                 else if(Character.isAlphabetic(password.charAt(i)))
                 {
                     hasAlphabetic = true;
                 }
                 else {
                     hasNonDigit = true;
                 }
             }

             int passWordQuality = DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED;

             if (hasDigit == true && hasAlphabetic == true && hasNonDigit == false) {
                 passWordQuality = DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC;
             } else if (hasDigit == false && hasAlphabetic == true && hasNonDigit == false) {
                 passWordQuality =  DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC;
             } else if (hasDigit == true && hasAlphabetic == false && hasNonDigit == false) {
                 passWordQuality =  DevicePolicyManager.PASSWORD_QUALITY_NUMERIC;
             }
             else if(hasNonDigit == true) {
                 passWordQuality =  DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
             }

             mDPM.setPasswordMinimumLetters(mAdminName, hasAlphabetic ? 1 : 0);
             mDPM.setPasswordMinimumNumeric(mAdminName, hasDigit ? 1 : 0);
             mDPM.setPasswordMinimumSymbols(mAdminName, hasNonDigit ? 1 : 0);

             mDPM.setPasswordQuality(mAdminName, passWordQuality);

             int result = mDPM.getPasswordQuality(mAdminName);

             if(result == passWordQuality)
             {
                 mStatusTextView.setText("Set password quality success.");
             }
             else
             {
                 mStatusTextView.setText("Set password quality failed.");
             }
         }
         else
         {
             mStatusTextView.setText("Error: No pin entered.");
         }


    }

    /**
     * In case of this application has not the dev admin rights
     * @return
     */
    public boolean enableAdmin(){
        if (!mDPM.isAdminActive(mAdminName)) {
            // try to become active – must happen here in this activity, to get result

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    mAdminName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Please give me admin rights...");
            startActivityForResult(intent, REQUEST_ENABLE);

            mStatusTextView.setText("Not in administrator mode.");

            return false;
        } else {
            // Already is a device administrator, can do security operations now.
            mStatusTextView.setText("Administrator mode");
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_ENABLE == requestCode)
        {
            if (resultCode == Activity.RESULT_OK) {
                // Has become the device administrator.
                mStatusTextView.setText("Administrator mode");
                initAll();
            } else {
                //Canceled or failed.
                mStatusTextView.setText("Not in administrator mode.");
            }
        }
    }
}
