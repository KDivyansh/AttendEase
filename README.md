**AttendEase: _A NFC based attendance system_**.

**Requirements to run the application**:
  :- A NFC enabled phone.
  :- NFC Card/Chip

**What it does?** **Workflow:-**
1. It asks new user to register.
   1. In the registeration process, the user enters there Name, Email and Enrollment ID.
   2. There is no field for password, but in the backend password is being sent to the firebase server. The password is actually the unique "device id" that is being extracted from your android device.
   3. This device id binds the users information with there device.

2. An NFC tap is required to launch the application.
   1. The application launches with a splash screen.
   2. Then the user logs in.
   3. Then it takes to a menu screen.
      1. Mark Attendance
      2. View Attendance
      3. Time Table

3. Mark Attendance:
   1. This takes the Code stored in the NFC card and a button is there Submit button.
   2. When this is submitted, the subject code taken from the NFC card.
   3. The timestamp is taken from the device directly, if the subject code is scheduled at that time, i.e. if time stamp matches the scheduled time of the Subject then attendance marking moves forward else gives an error.
   4. If time stamp is matched then the Subject code is matched to the code in the Database(firebase) on the user's id and it is incremented by 1, thus marking the attendance.

4. View Attendance:
   1. This is a simple page that brings the user's attendance from the database and is displayed on the screen in a tabular format.

5. Time Table:
   1. This was also a simple screen where the weekly schedule of subjects can be seen.


The application's first focus is to prevent proxy as once the student/user signs in using their personal device, they can't won't be able to login using any other device.

Future scope:
  1. Mostly things are hardcoded rightnow, and our team will try to make this application more dynamic.
  2. Everytime the application opens, the user have to login which will be taken care of in the near future.
  3. There is no admin panel currently and we will be working on making one.
  4. We are also looking forward to add hashing so that even if someone can get access to database they wont be able to make changes what they desire.
  5. The application is lacking dynamic use and we look forward to meet that need.
  6. This application can be implement on the current existing system easily for events like concerts, parties etc.

With this I conclude my project, this was a interesting project that we build for our minor project in semester 6 and 7. This taught about new technology of NFC and enhanced the knowledge of android development.
