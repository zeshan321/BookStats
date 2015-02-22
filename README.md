# MySQL connection. If 'Use MySQL' is set to false BookStats will use flat file.
MySQL:
    Use MySQL: false
    IP Address: localhost
    Port: 3306
    User: notroot
    Pass: notmypassword
    Database: BookStats

# How often BookStats will save data. In minutes.
Save Interval: 2

# Book customization
Book:
- "&b&l{Player}'s Stats!"
- " "
- "&1Balance: &4{Balance}"
- " "
- "&1Kills: &4{Kills}"
- " "
- "&1Deaths: &4{Deaths}"
- " "
- "&1Kill Streak: &4{Killstreak}"
- " "
- "&1Mob Kills: &4{Mobkills}"
- " "
- "&1Block Placed: &4{Blocksplaced}"
- " "
- "&1Block Broken: &4{Blocksbroken}"

Book Title: "&6{Player}'s Stats"
Book Author: "&aBookStats"

On Join:
    # Give BookStats on join 
    Give Book: false
    # Set which slot the book should be put in. If set to 0 it will just add it to the players inventory.
    Slot: 0

# Update BookStat books every time it's opened.
Update Book: true
