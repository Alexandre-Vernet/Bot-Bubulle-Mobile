# Bot-Bubulle

Daily Notification System with Scheduled Time


![icon](https://user-images.githubusercontent.com/72151831/109391919-cae3c480-7919-11eb-8338-929ae4af2fdd.png)



## Description
The user sets a specific time for sending notifications. Starting from that moment, a notification will be sent every day at the scheduled time. At any time, the user can change the notification time, and the notification will be sent at the new scheduled time.



## Loading Screen
Upon opening the application, an animated loading screen appears for 2 seconds.


![loading](https://user-images.githubusercontent.com/72151831/109391659-7a1f9c00-7918-11eb-8b50-4f0b203f8a43.jpg)


## Schedule Notification Time
From the main screen, the user can schedule a time to receive notification

![activity_main](https://user-images.githubusercontent.com/72151831/109391715-c79c0900-7918-11eb-90c8-b7e57d6cd1c2.jpg)



## Received Notification
When the user receives the notification, he can click the "Send Message" button to automatically send a pre-filled SMS.

![notification](https://user-images.githubusercontent.com/72151831/109867375-b29bde80-7c66-11eb-9b3b-260820737a9f.jpg)



## Persist data
When the notification time is changed, the time is stored in memory, allowing the phone to reset the notification time upon restarting by reading this data. This data is also used to set the clock hands (activity_main) to the time of the next notification.
