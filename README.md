# ActivityCoins

[![Build Status](http://ci.colorizedmind.de/job/ActivityCoins/badge/icon)](http://ci.colorizedmind.de/job/ActivityCoins/)

With ActivityCoins you'll get rewarded for ingame activity. Players will get paid based on their activity in a predefined interval. We're using this plugin on our server for several years.

The following activities are available:

* Breaking blocks (survival & creative)
* Placing blocks (survival & creative)
* Chatting (survival & creative)
* Using commands (survival & creative)
* Fishing (survival)
* Killing mobs (survival)

This project is also available on:

* [Spigot](https://www.spigotmc.org/resources/activitycoins.29296/)

## Features

* You can set the worth for each activity individually
* You can disable each activity completely
* You can define the payout interval
* You can define the minimum payout amount (= 0% activity)
* You can define the maximum payout amount (= 100% activity)

## Commands

* `/activity` - Display current activity

## Permissions

There are no Permissions required to run ActivityCoins.

## Configuration

| Key                        | Type    | Default | Description                                                   |
| -------------------------- | ------- | ------- | ------------------------------------------------------------- |
| `interval`                 | int     | 15      | Payout interval in minutes                                    |
| `blockLocHistorySize`      | int     | 5       | History size for previously placed / broken blocks per player |
| `worth.blockBreakSurvival` | double  | 1.0     | Worth for breaking blocks in survival mode                    |
| `worth.blockBreakCreative` | double  | 0.5     | Worth for breaking blocks in creative mode                    |
| `worth.blockPlaceSurvival` | double  | 2.0     | Worth for placing blocks in survival mode                     |
| `worth.blockPlaceCreative` | double  | 1.0     | Worth for placing blocks in creative mode                     |
| `worth.chat`               | double  | 1.0     | Worth for chatting                                            |
| `worth.command`            | double  | 0.1     | Worth for using commands                                      |
| `worth.fishing`            | double  | 70.0    | Worth for fishing                                             |
| `worth.kill`               | double  | 4.0     | Worth for killing mobs                                        |
| `worth.max`                | double  | 1000.0  | Worth which stands for 100% activity                          |
| `income.min`               | double  | 0.0     | Income for 0% activity                                        |
| `income.max`               | double  | 500.0   | Income for 100% activity                                      |
| `logging`                  | boolean | true    | Enable or disable logging                                     |
| `announce`                 | boolean | true    | Enable or disable payout announcements                        |
