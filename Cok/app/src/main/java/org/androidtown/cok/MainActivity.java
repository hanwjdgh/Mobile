package org.androidtown.cok;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ImageButton mbutton;
    String phoneNum;
    int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;
    Server server = new Server();
    public static Map<String, Integer> Ala = new HashMap<String, Integer>();
    String setCurDate;
    int cnt = 0;
    public static HashMap<String, String> location = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SplachActivity.class));
        locationSetup();
        phoneNum = getPhoneNum();
        setDate();
        new Thread() {
            @Override
            public void run() {
                System.out.println("!!!");
                HttpURLConnection con = server.getConnection("GET", "/phonenum/" + phoneNum);
                System.out.println("Connection done");
                try {
                    System.out.println("codeasd " + con.getResponseCode());
                    arrayToobject(server.readJson(con));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


        mbutton = (ImageButton) findViewById(R.id.m_button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat SettingFormat = new SimpleDateFormat("yyyy-MM-dd");
        setCurDate = SettingFormat.format(date);
    }

    public String getPhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String outName = data.getStringExtra("title");
            String people = data.getStringExtra("people");
            String num = data.getStringExtra("number");
            String start = data.getStringExtra("start");
            String finish = data.getStringExtra("finish");

            int cur = calculate(start, setCurDate);
            if (outName.length() > 0 && num.length() > 0 && people.length() > 0) {
                InsertMap(start, finish);
                makefragment(phoneNum, outName, people, num, calculate(start, finish) + "", cur, finish);
                server.Insertproject(phoneNum, phoneNum, outName, num, start, finish, 0, Integer.parseInt(people));
                String title = phoneNum.replace("+", outName);
                server.maketable(title, VoteActivtiy.data);
            }
        }
    }

    public void InsertMap(String start, String finish) {
        String[] arr1 = start.split("-");
        String str;
        int tem = calculate(start, finish);
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);
        for (int j = 0; j < tem; j++) {
            if (mon < 10) {
                if (day < 10)
                    str = year + "-" + "0" + mon + "-" + "0" + day;
                else
                    str = year + "-" + "0" + mon + "-" + day;
            } else {
                if (day < 10)
                    str = year + "-" + mon + "-" + "0" + day;
                else
                    str = year + "-" + mon + "-" + day;
            }
            VoteActivtiy.data.put(str, 0);

            if (mon == 2 && day == 28) {
                mon += 1;
                day = 1;
            } else if ((mon == 4 || mon == 6 || mon == 9 || mon == 11) && day == 30) {
                mon += 1;
                day = 1;
            } else if ((mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) && day == 31) {
                if (mon == 12)
                    mon = 1;
                else
                    mon += 1;
                day = 1;
            } else
                day++;
        }
    }

    public int calculate(String start, String finish) {
        String[] arr1 = start.split("-");
        String[] arr2 = finish.split("-");
        int stem = 0, ftem = 0;
        for (int i = 0; i < Integer.parseInt(arr1[1]) - 1; i++) {
            stem += arr[i];
        }
        stem += Integer.parseInt(arr1[2]);
        for (int i = 0; i < Integer.parseInt(arr2[1]) - 1; i++) {
            ftem += arr[i];
        }
        ftem += Integer.parseInt(arr2[2]);
        if (ftem - stem >= 0)
            return ftem - stem;
        else
            return -1;
    }

    public void makefragment(final String master, final String outName, String peo, String num, String day, int cur, String finish) {
        System.out.print("@@@@@"+master+" "+outName+" "+peo+" "+num+" "+day+" "+cur+" "+finish);
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = server.getConnection("GET", "/galarm/" + master + "/" + phoneNum + "/" + outName);
                try {
                    System.out.println("codef" + conn.getResponseCode());
                    setalarm(server.readJson(conn));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        MainFragment cf = new MainFragment(MainActivity.this, Ala);
        Bundle bundle = new Bundle();
        bundle.putString("master", master);
        bundle.putString("Project", outName);
        bundle.putString("mCount", peo);
        bundle.putString("mcount", num);
        bundle.putString("day", day);
        bundle.putInt("cur", cur);
        bundle.putString("finish", finish);
        cf.setArguments(bundle);
        tr.add(R.id.frame, cf, "counter");
        tr.commit();
    }

    public void makeAlarm(String name, String finish) {
        int dis = 1;
        String[] spl = finish.split("-");
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceive.class);
        PendingIntent pendingIntent;
        int year = Integer.parseInt(spl[0]);
        int mon = Integer.parseInt(spl[1]);
        int date = Integer.parseInt(spl[2]);
        for (String key : Ala.keySet()) {
            if (Ala.get(key) == 1) {
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, cnt, receiverIntent, 0);
                calendar.set(year,mon-1,date-dis);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                dis += 2;
                cnt++;
            }
        }
    }


    private void setalarm(JSONArray jsonArray) throws JSONException {
        JSONObject order = jsonArray.getJSONObject(0);
        Ala.put("1", order.getInt("alarm1"));
        Ala.put("3", order.getInt("alarm3"));
        Ala.put("5", order.getInt("alarm5"));
        Ala.put("7", order.getInt("alarm7"));
        //makeAlarm(order.getString("project"), order.getString("finish"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG", "Permission Granted");
                    //Proceed to next steps
                } else {
                    Log.e("TAG", "Permission Denied");
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            makefragment(order.getString("master"), order.getString("project"), order.getInt("people") + "", order.getInt("meeting") + "", calculate(order.getString("start"), order.getString("finish")) + "", calculate(order.getString("start"), setCurDate), order.getString("finish"));
        }
    }
    public void locationSetup(){
        location.put("문산","37.8547, 126.7874");
        location.put("파주"," 37.8154, 126.7924");
        location.put("양평중앙선", "37.5339, 127.5047");
        location.put("월릉","37.7962, 126.7926");
        location.put("금촌","37.7663, 126.7745");
        location.put("금릉","37.7514, 126.7654");
        location.put("운정","37.7256, 126.7671");
        location.put("탄현", "37.6944, 126.7611");
        location.put("일산", "37.6825, 126.7695");
        location.put("풍산", "37.6723, 126.7863");
        location.put("백마", "37.6585, 126.7944");
        location.put("곡산", "37.6459, 126.8019");
        location.put("대곡", "37.6316, 126.8112");
        location.put("능곡", "37.6187, 126.8208");
        location.put("행신", "37.6121, 126.8340");
        location.put("화정", "37.6346, 126.8326");
        location.put("수색", "37.5808, 126.8956");
        location.put("디지털미디어시티" ,"37.5779, 126.9003");
        location.put("가좌", "37.5686, 126.914844");
        location.put("홍대입구", "37.5580, 126.9254");
        location.put("서강대", "37.5521, 126.9353");
        location.put("공덕", "37.5426, 126.9521");
        location.put("신촌", "37.5598, 126.9423");
        location.put("삼각지", "37.5345, 126.9729");
        location.put("서울역", "37.5547, 126.9707");
        location.put("용산", "37.5299, 126.9647");
        location.put("이촌", "37.5225, 126.9738");
        location.put("서빙고" ,"37.5196, 126.9883");
        location.put("한남","37.5293, 127.0089");
        location.put("옥수", "37.5405, 127.0186");
        location.put("응봉", "37.5504, 127.0348");
        location.put("왕십리", "37.5611, 127.0354");
        location.put("청량리", "37.5803, 127.0469");
        location.put("회기", "37.5898, 127.0579");
        location.put("중랑", "37.5949, 127.0761");
        location.put("상봉", "37.5925, 127.0858");
        location.put("망우", "37.5993, 127.0923");
        location.put("양원", "37.6066, 127.1080");
        location.put("구리", "37.6032, 127.1432");
        location.put("도농", "37.6087, 127.1611");
        location.put("양정", "37.6042, 127.1946");
        location.put("덕소", "37.5869, 127.2088");
        location.put("도심", "37.5796, 127.2228");
        location.put("팔당", "37.5472, 127.2438");
        location.put("운길산", "37.5546, 127.3101");
        location.put("양수", "37.5460, 127.3288");
        location.put("신원", "37.5258, 127.3727");
        location.put("국수", "37.5162, 127.3994");
        location.put("아신", "37.5139, 127.4432");
        location.put("오빈", "37.5061, 127.4740");
        location.put("양평중앙선", "37.5339, 127.5047");
        location.put("원덕", "37.4685, 127.5474");
        location.put("용문", "37.4820, 127.5942");
        location.put("대화", "37.6759, 126.7477");
        location.put("주엽", "37.6701, 126.7612");
        location.put("정발산", "37.6597, 126.7732");
        location.put("마두","37.6521, 126.7776");
        location.put("백석", "37.6429, 126.7881");
        location.put("화정", "37.6346, 126.8326");
        location.put("원당", "37.6531, 126.8428");
        location.put("삼송", "37.6531, 126.8955");
        location.put("지축", "37.6480, 126.9136");
        location.put("구파발", "37.6365, 126.9188");
        location.put("연신내", "37.6190, 126.9212");
        location.put("불광", "37.6100, 126.9302");
        location.put("녹번", "37.6008, 126.9357");
        location.put("홍제", "37.5888, 126.9440");
        location.put("무악재", "37.5826, 126.9501");
        location.put("독립문", "37.5744, 126.9578");
        location.put("경복궁", "37.5758, 126.9735");
        location.put("안국", "37.5765, 126.9854");
        location.put("종로3가", "37.5704, 126.9921");
        location.put("을지로3가", "37.5662, 126.9926");
        location.put("충무로", "37.5609, 126.9935");
        location.put("동대입구", "37.5590, 127.0050");
        location.put("약수", "37.5649, 127.0146");
        location.put("금호", "37.5482, 127.0158");
        location.put("압구정", "37.5261, 127.0284");
        location.put("신사", "37.5162, 127.0196");
        location.put("잠원", "37.5129, 127.0115");
        location.put("고속터미널", "37.5049, 127.0049");
        location.put("교대", "37.4929, 127.0137");
        location.put("남부터미널", "37.4849, 127.0162");
        location.put("양재", "37.4845, 127.0340");
        location.put("매봉", "37.4870, 127.0469");
        location.put("도곡", "37.4909, 127.0553");
        location.put("대치", "37.4945, 127.0634");
        location.put("학여울", "37.4960, 127.0705");
        location.put("대청", "37.4935, 127.0794");
        location.put("일원", "37.4838, 127.0841");
        location.put("수서", "37.4876, 127.1011");
        location.put("가락시장", "37.4924, 127.1187");
        location.put("경찰병원", "37.4956, 127.1241");
        location.put("오금", "37.5020, 127.1282");
        location.put("암사", "37.5501, 1271275");
        location.put("천호", "37.5386, 1271235");
        location.put("강동구청", "37.5304, 127.1205");
        location.put("몽촌토성", "37.5176, 127.1127");
        location.put("잠실", "37.5132, 127.1001");
        location.put("석촌", "37.5054, 127.1069");
        location.put("송파", "37.4997, 127.1122");
        location.put("문정","37.4860, 127.1224");
        location.put("장지", "37.4787, 127.1261");
        location.put("복정", "37.4697, 127.1266");
        location.put("산성", "37.4565, 127.1500");
        location.put("남한산성입구", "37.4515, 127.1598");
        location.put("단대오거리", "37.4450, 127.1567");
        location.put("신흥", "37.4409, 127.1474");
        location.put("수진", "37.4376, 127.1409");
        location.put("모란", "37.4321, 127.1290");
        location.put("전대에버랜드", "37.2854, 127.2194");
        location.put("둔전", "37.2672, 127.2135");
        location.put("보평", "37.2589, 127.2185");
        location.put("고진", "37.2446, 127.2141");
        location.put("운동장송담대", "37.2379, 127.2090");
        location.put("김량장", "37.2372, 127.1986");
        location.put("명지대", "37.2380, 127.1902");
        location.put("시청용인대", "37.2394, 127.1788");
        location.put("삼가", "37.2420, 127.1681");
        location.put("초당", "37.2610, 127.1590");
        location.put("동백", "37.2691, 127.1527");
        location.put("어정", "37.2753, 127.1437");
        location.put("지석", "37.2696, 127.1366");
        location.put("강남대", "37.2701, 127.1260");
        location.put("기흥", "37.2757, 127.1158");
        location.put("서울숲", "37.5436, 127.0446");
        location.put("압구정로데오", "37.5274, 127.0405");
        location.put("강남구청", "37.5171, 127.0412");
        location.put("선정릉", "37.5103, 127.0437");
        location.put("선릉", "37.5045, 127.0489");
        location.put("합정" ,"37.5495, 126.9140");
        location.put("한티", "37.4961,127.0528");
        location.put("구룡", "37.4870, 127.0594");
        location.put("개포동", "37.4891, 127.0663");
        location.put("대모산입구", "37.4913, 127.0727");
        location.put("가천대", "37.4487, 127.1267");
        location.put("태평" ,"37.4398, 127.1277");
        location.put("야탑", "37.4113, 127.1286");
        location.put("이매", "37.3958, 127.1282");
        location.put("서현", "37.3849, 127.1232");
        location.put("수내", "37.3784, 127.1141");
        location.put("정자", "37.3660, 127.1080");
        location.put("미금", "37.3500, 127.1089");
        location.put("오리", "37.3398, 127.1089");
        location.put("죽전", "37.3245, 127.1073");
        location.put("보정", "37.3127, 127.1081");
        location.put("구성", "37.2990, 127.1056");
        location.put("신갈", "37.2861, 127.1112");
        location.put("상갈", "37.2618, 127.1087");
        location.put("청명", "37.2594, 127.0789");
        location.put("영통", "37.2538, 127.0738");
        location.put("망포", "37.2458, 127.0573");
        location.put("매탄권선", "37.2524, 127.0408");
        location.put("수원시청", "37.2619, 127.0306");
        location.put("매교", "37.2654, 127.0156");
        location.put("수원", "37.2661, 126.9998");
        location.put("강남", "37.4979, 127.0275");
        location.put("양재시민의숲", "37.4700, 127.0383");
        location.put("청계산입구", "37.4482, 127.0547");
        location.put("판교" ,"37.3948, 127.1111");
        location.put("춘천", "37.8844, 127.7165");
        location.put("남춘천", "37.8640, 127.7237");
        location.put("김유정", "37.8184, 127.7142");
        location.put("강촌", "37.8056, 127.6339");
        location.put("백양리", "37.8308, 127.5890");
        location.put("굴봉산", "37.8321, 127.5577");
        location.put("가평", "37.8145, 127.5107");
        location.put("상천", "37.7704, 127.4542");
        location.put("청평", "37.7355, 127.4265");
        location.put("대성리", "37.6839, 127.3791");
        location.put("마석", "37.6523, 127.3117");
        location.put("천마산", "37.6590, 127.2857");
        location.put("평내호평", "37.6531, 127.2444");
        location.put("금곡", "37.6374, 127.2079");
        location.put("사릉", "37.6511, 127.1768");
        location.put("퇴계원", "37.6483, 127.1438");
        location.put("별내", "37.6422, 127.1272");
        location.put("갈매", "37.6341, 127.1147");
        location.put("신내", "37.6128, 127.1032");
        location.put("개화","37,5786, 126.7974");
        location.put("공항시장","37.5637, 126.8106");
        location.put("신방화","37.5675, 126.8166");
        location.put("마곡나루","37.5668, 126.8271");
        location.put("양천향교", "37.5680, 126.8419");
        location.put("가양", "37.5615, 126.8542");
        location.put("증미", "37.5581, 126.8605");
        location.put("등촌", "37.5512, 126.8645");
        location.put("염창", "37.5372, 126.8737");
        location.put("신목동", "37.5460, 126.8814");
        location.put("선유도", "37.5378, 126.8939");
        location.put("국회의사당", "37.5281, 126.9178");
        location.put("샛강", "37.5168, 126.9288");
        location.put("노량진9호선", "37.5140, 126.9415");
        location.put("노들", "37.5128, 126.9493");
        location.put("흑석" ,"37.5090, 126.9634");
        location.put("동작", "37.5028, 126.9803");
        location.put("구반포", "37.5019, 126.9899");
        location.put("신반포", "37.5035, 126.9960");
        location.put("사평", "37.5045, 127.0147");
        location.put("신논현", "37.5044, 1270245");
        location.put("송도", "37.4296, 126.6545");
        location.put("연수", "37.4177, 126.6789");
        location.put("원인재", "37.4131, 126.6864");
        location.put("남동인더스파크", "37.4078, 126.6953");
        location.put("호구포", "37.4016, 126.7086");
        location.put("인천논현", "37.4007, 126.7225");
        location.put("소래포구", "37.4009, 126.7334");
        location.put("월곶", "37.3919, 126.7427");
        location.put("오이도", "37.3618, 126.7384");
        location.put(" 국제업무지구", "37.4004, 126.6301");
        location.put("센트럴파크", "37.3927, 126.6350");
        location.put("인천대입구", "37.3862, 126.6393");
        location.put("지식정보단지","37.3780,126.6454");
        location.put("테크노파크","37.3823,126.6563");
        location.put("캠퍼스타운","37.3881,126.6620");
        location.put("동막","37.3984,126.6733");
        location.put("동춘","37.4046,126.6809");
        location.put("신연수","37.4180,126.6940");
        location.put("선학","37.4269,126.6989");
        location.put("문학경기장","37.4349,126.6983");
        location.put("인천터미널","37.4412,126.7011");
        location.put("예술회관","37.4493,126.7009");
        location.put("인천시청","37.4576,126.7022");
        location.put("간석오거리","37.4661,7082");
        location.put("부평삼거리","37.4759,126.7093");
        location.put("동수","37.485401,126.7814");
        location.put("부평","37.4895,126.7246");
        location.put("부평시장","37.4949,126.7230");
        location.put("부평구청","37.5084,126.7206");
        location.put("갈산","37.5171,1267215");
        location.put("작전","37.5302,126.7225");
        location.put("경인교대입구","37.5381,126.7225");
        location.put("계산","37.5432,126.7285");
        location.put("임학","37.5451,126.7385");
        location.put("박촌","37.5535,1267449");
        location.put("귤현","37.5664,126.7426");
        //5호선
        location.put("마천","37.4950,127.1528");
        location.put("거여","37.4934,127.1436");
        location.put("개롱","37.4981,127.1346");
        location.put("방이","37.5085,127.1259");
        location.put("올림픽공원","37.5166,127.1311");
        location.put("둔촌동","37.5276,127.1362");
        location.put("강동","37.5359,127,1321");
        location.put("길동","37.5378,127.1399");
        location.put("굽은다리","37.5454,127.142927");
        location.put("명일","37.5513,127.1441");
        location.put("고덕","37.5550,127.1540");
        location.put("상일동","37.5565,127.1654");
        location.put("광나루","37.5453,127.1035");
        location.put("아차산","37.5520,127.0896");
        location.put("군자","37.5571,127.0794");
        location.put("장한평","37.5614,127.0646");
        location.put("답십리","37.5670,127.0523");
        location.put("마장","37.5661,127.0429");
        location.put("행당","37.5573,127.0296");
        location.put("신금호","37.5545,127.0208");
        location.put("청구","37.5601,127.0138");
        location.put("동대문역사문화공원","37.5656,127.0091");
        location.put("을지로4가","37.5674,126.9980");
        location.put("광화문","37.5716,126.9764");
        location.put("서대문","37.5657,126.9666");
        location.put("충정로","37.5603,126.9629");
        location.put("애오개","37.5535,126.9566");
        location.put("마포","37.5397,126.9460");
        location.put("여의나루","37.5272,126.9327");
        location.put("여의도","37.5217,126.9242");
        location.put("신길","37.5170,126.9177");
        location.put("영등포시장","37.5227,126.9051");
        location.put("영등포구청","37.5257,126.8966");
        location.put("오목교","37.5245,126.8750");
        location.put("목동","37.5261,126.8642");
        location.put("신정","37.5250,126.8561");
        location.put("까치산","37.5313,126.8469");
        location.put("화곡","37.5415,1268404");
        location.put("우장산","37.5488,126.8363");
        location.put("발산","37.5587,126.8376");
        location.put("마곡","37.5602,126.8246");
        location.put("송정","37.5612,126.8121");
        location.put("김포공항","37.5618,126.8019");
        location.put("개화산","37.5724,126.8068");
        location.put("방화","37.5776,126.8128");
        //공항선
        location.put("인천국제공항","37.4474,126.4524");
        location.put("공항화물청사","37.4591,126.4773");
        location.put("운서","37.4929,126.4936");
        location.put("청라국제도시","37.5563,126.6246");
        location.put("검암","37.5692,126.6736");
        location.put("계양","37.5713,126.7364");
        location.put("인천","37.4763,126.6169");
        location.put("동인천","37.4754,126.632641");
        location.put("도원","37.4686,126.6428");
        location.put("제물포","37.4668,126.6571");
        location.put("도화","37.4661,126.6684");
        location.put("주안","37.4649,126.6806");
        location.put("간석","37.4646,126.6933");
        location.put("동암","37.4708,126.7029");
        location.put("백운","37.4833,126.7071");
        location.put("부개","37.4884,126.7405");
        location.put("송내","37.4876,126.7530");
        location.put("중동","37.4886,126.7645");
        location.put("부천","37.4840,126.7828");
        location.put("소사","37.4827,126.7956");
        location.put("역곡","37.4851,126.8115");
        location.put("오류동","37.494417,126.8445");
        location.put("온수","37.4923,126.8233");
        location.put("개봉","37.4946,126.8584");
        location.put("구일","37.4962,126.8695");
        location.put("구로","37.5032,126.8820");

        //1호선
        location.put("독산","37.4659,126.8895");
        location.put("광명","37.4159,126.8845");
        location.put("가산디지털단지","37.4797,126.8838");
        location.put("금천구청","37.4557,126.8944");
        location.put("석수","37.4351,126.9021");
        location.put("관악","37.4196,126.9085");
        location.put("안양","37.4019,126.9226");
        location.put("명학","37.3844,126.9355");
        location.put("금정","37.3719,126.9436");
        location.put("군포","37.3537,126.9484");
        location.put("당정","37.3433,126.9483");
        location.put("의왕","37.3206,1269480");
        location.put("성균관대","37.3003,126.9708");
        location.put("화서","37.2840,126.9895");
        location.put("세류","37.2441,127.0138");
        location.put ("병점","37.2068,127.0332");
        location.put("세마","37.1873,127.0433");
        location.put("오산대","37.1694,127.0631");
        location.put("오산","37.1455,127.0667");
        location.put("진위","37.1098,127.0623");
        location.put("송탄","37.0754,127.0544");
        location.put("서정리","37.0564,127.0528");
        location.put("지제","37.0188,127.0702");
        location.put("평택","36.9905,127.0855");
        location.put("성환","36.9157,127.1270");
        location.put("직산","36.8709,127.1437");
        location.put("두정","36.8337,127.1488");
        location.put("천안","38.8093,127.1462");
        location.put("봉명","36.8016,127.1363");
        location.put("쌍용","36.7937,127.1213");
        location.put("아산","36.7922,127.1054");
        location.put("배방","36.7775,127.0527");//배양x 배방o
        location.put("온양온천","36.7804,127.0037");
        location.put("신창","36.7691,");
        location.put("신도림","37.5090,126.8915");
        location.put("영등포","37.5157,126.9074");
        location.put("대방","37.5134,126.9264");
        location.put("노량진","37.5140");
        location.put("남영","37.5406,126.9712");
        location.put("어린이대공원","37.5478,127.0746");
        location.put("건대입구","37.5404,127.0692");
        location.put("뚝섬유원지","37.5315,127.0667");
        location.put("청담","37.5191,127.0519");
        location.put("학동","37.5142,127.0316");
        location.put("논현","37.5112,127.0217");
        location.put("반포","37.5082,127.0118");
        location.put("내방","37.4877,126.9935");
        location.put("총신대입구","37.4866,126.9821");
        location.put("남성","37.4848,126.9706");
        location.put("숭실대입구","37.4963,126.9535");
        location.put("상도장승배기","37.5048,126.9392");//상도장승배기x , 장승배기o
        location.put("신대방삼거리","37.4996,126.9281");
        location.put("보라매","37.4999,126.9203");
        location.put("신풍","37.5001,126.9089");
        location.put("대림","37.4933,126.8949");
        location.put("남구로","37.4864,126.8875");
        location.put("철산","37.4759,126.8683");
        location.put("광명사거리","37.4793,126.8546");
        location.put("천왕","37.48671,126.8387");
        location.put("까치울","37.5061,126.8109");
        location.put("부천종합운동장","37.5059,126.7989");
        location.put("춘의","37.5037,126.7870");
        location.put("신중동","37.5030,126.7758");
        location.put("부천시청","37.5046,126.7640");
        location.put("상동","37.5058,126.7531");
        location.put("삼선체육관","37.5064,1267420");
        location.put("굴포천","37.5069,126.7314");

        //4호선

        location.put("정왕 ","37.3518,126.7428");
        location.put("신길온천","37.3374,126.7672");
        location.put("안산","37.3270,126.7887");
        location.put("초지","37.3205,126.8061");
        location.put("고잔","37.3167,126.8231");
        location.put("중앙","37.316055,126.838489");
        location.put("한대앞","37.3096,126.8535");
        location.put("상록수","37.3028,126.8665");
        location.put("반월","37.3122,126.9036");
        location.put("대야미","37.3283,126.9171");
        location.put("수리산","37.3503,126.9255");
        location.put("산본","37.358,126.9329");
        location.put("범계","37.3897,126.9508");
        location.put("평촌","37.3943,126.9638");
        location.put("인덕원","37.4018,126.9768");
        location.put("정부과천청사","37.4264,126.9897");
        location.put("과천","37.4431,126.9963");
        location.put("대공원","37.4357,127.0064");
        location.put("경마공원","37.4439,127.0078");
        location.put("선바위","37.4518,127.0021");
        location.put("남태령","37.4642,126.9891");
        location.put("사당","37.4765,12.69816");
        location.put("숙대입구","37.5450,126.9719");
        location.put("회현","37.5585,126.9781");
        location.put("명동","37.5609,126.9864");
        location.put("혜화","37.5821,127.0018");
        location.put("한성대입구","37.5882,127.0057");
        location.put("성신여대입구","37.5927,127.0165");
        location.put("길음","37.6032,127.0249");
        location.put("미아사거리","37.6132,127.0300");
        location.put("미아","37.6264,127.0261");
        location.put("수유","37.6376,127.0250");
        location.put("쌍문","37.6482,127.0343");
        location.put("상계","37.6605,127.0732");
        location.put("당고개","37.6703,127.0791");

//6호선
        location.put("봉화산","37.6173,127.0913");
        location.put("화랑대","37.6198,127.0836");
        location.put("돌곶이","37.6104,127.0563");
        location.put("상월곡","37.6066,127.0488");
        location.put("월곡","37.6019,127.0415");
        location.put("고려대","37.5903,127.0358");
        location.put("안암","37.5864,127.0288");
        location.put("보문","37.5852,127.0193");
        location.put("창신","37.5793,127.0152");
        location.put("신당","37.5657,127.0206");
        location.put("버티고개","37.5481,127.0071");
        location.put("한강진","37.5395,127.0017");
        location.put("이태원","37.5345,126.9946");
        location.put("녹사평","37.5350,126.9859");
        location.put("효창공원앞","37.5393,126.126.9613");
        location.put("대흥","37.5476,126.9423");
        location.put("광흥창","37.5475,126.9319");
        location.put("상수","37.5477,126.9229");
        location.put("망원","37.5560,126.9101");
        location.put("마포구청","37.5633,126.9034");
        location.put("월드컵경기장","37.5696,126.8992");
        location.put("증산","37.5838,126.9097");
        location.put("새절","37.5910,126.9136");
        location.put("응암","37.5984,126.9156");
        location.put("역촌","37.6063,126.9229");
        location.put("독바위","37.6183,1269330");
        location.put("구산","37.6113,126.9173");

        //2호선
        location.put("신정네거리","37.5202,126.8527");
        location.put("양천구청","37.5123,126.8655");
        location.put("도림천","37.5144,126.8825");
        location.put("구로디지털단지","37.4852,126.9013");
        location.put("신대방","37.4875,126.9132");
        location.put("신림","37.4852,126.9297");
        location.put("봉천","37.482476,126.941542");
        location.put("서울대입구","37.4812,126.9527");
        location.put("낙성대","37.4771,126.9634");
        location.put("방배","37.4815,126.9976");
        location.put("서초","37.4918,127.0076");
        location.put("역삼","37.5007,127.0369");
        location.put("삼성","37.5089,127.0631");
        location.put("종합운동장","37.5110,127.0738");
        location.put("신천","37.5116,0855");//잠실새내일수도
        location.put("잠실나루","37.5206,127.1038");
        location.put("강변","37.5351,127.0946");
        location.put("구의","37.5371,127.0861");
        location.put("성수","37.5445,127.0559");
        location.put("용답","37.5620,127.0508");
        location.put("신답","37.5699,127.0469");
        location.put("용두","37.5739,127.0387");
        location.put("뚝섬","37.5472,127.0473");
        location.put("한양대","37.5556,127.0436");
        location.put("상왕십리","37.5645,127.0288");
        location.put("을지로입구","37.5560,126.9826");
        location.put("아현","37.5573,126.9560");
        location.put("이대","37.5567,126.9458");
        location.put("신촌2호선","37.5562,126.9399");
        location.put("문래","37.5179,126.8948");

        //7호선윗쪽
        location.put("중곡","37.5655,127.0840");
        location.put("용마산","37.5737,127.0868");
        location.put("사가정","37.5809,127.0885");
        location.put("면목","37.5887,127.0875");
        location.put("중화","37.6025,127.0792");
        location.put("먹골","37.6107,127.0776");
        location.put("태릉입구","37.6175,127.0745");
        location.put("공릉","37.6258,127.0729");
        location.put("중계","37.6455,127.0639");
        location.put("하계","37.6359,127.0682");
        location.put("노원","37.6564,127.0633");
        location.put("수락산","37.6779,127.0552");
        location.put("도봉산","37.6896,127.0463");
        location.put("장암","37.6996,127.0523");

    }
}