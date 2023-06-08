# PoisonousMushroom (Spigot, Paper 1.20)
## 목차
- [1. 소개](#소개)
- [2. 플러그인 설명](#플러그인-설명)
- [3. 승리 조건](#승리-조건)
- [4. 안내](#안내)
- [5. 추가 사이트](#추가-사이트)
- [6. Contact Email](#Contact-Email)
- [7. Special Thanks](#Special-Thanks)

## 소개
- PoisonousMushroom 플러그인입니다. Minecraft Spigot, Paper 버킷 1.20 버전에서 사용이 가능합니다.
- 주의 : Paper API로 만들어졌으므로, Spigot에서 오류가 발생할 수 있습니다.

## 플러그인 설명
- 버킷 Main 폴더에서 plugins 폴더에 [PoisonousMushroom.jar](https://github.com/yj0524/PoisonousMushroom/releases/latest/download/PoisonousMushroom.jar) 파일을 넣으면 PoisonousMushroom 폴더가 생깁니다.
- PoisonousMushroom 폴더 안에 config.yml 파일을 열어 아래의 값을 설정할 수 있습니다.
- huskHealth : 일반 시민이 죽었을 때 생성할 허스크의 체력을 설정합니다.
- huskCount : 일반 시민이 죽었을 때 생성할 허스크의 마릿수를 설정합니다.
- mushroomPlayerName : 독버섯을 할 플레이어의 이름을 설정합니다.
- serverAutoShutDown : 서버 자동 종료 여부를 설정합니다.
- serverShutDownTick : 서버 자동 종료 시간을 설정합니다.
- mobFollowRange : 몬스터의 추적 거리를 설정합니다.
- respawnSpectatorRange : 부활 신호기를 사용한 사람의 반경에 있는 사망자를 부활시키는 거리를 설정합니다.
- mobSpawn : 몬스터 스폰 여부를 설정합니다.
- huskTridentPercent : 허스크를 잡았을 때, 삼지창을 드랍할 확률을 설정합니다.
- worldBorderSize : 월드 보더의 크기를 설정합니다.
- worldBorderEnable : 월드 보더의 활성화 여부를 설정합니다.
- endGateway : 엔드 게이트웨이 생성 여부를 설정합니다.
- randomSpawn : 랜덤 스폰 여부를 설정합니다.
- peopleHealth : 생존자의 체력을 설정합니다.
- mushroomHealth : 버섯의 체력을 설정합니다.
- superMushroomHealth : 슈퍼 버섯의 체력을 설정합니다.
- sacrificePercent : 희생 성공 확률을 설정합니다.
- infectionPercent : 감염 성공 확률을 설정합니다.
- infectionEnable : 감염 기능 활성화 여부를 설정합니다.
- gameEndMessageEnable : 게임 종료 히든 메시지 활성화 여부를 설정합니다.
- informationEnable : 플러그인 정보 스코어보드 활성화 여부를 설정합니다.
- People 팀에 Join 되어있다면, 죽었을 때 Spectator 팀으로 Join 됨과 동시에 게임 모드가 Spectator 모드로 변경됩니다.
- 만일 Mushroom 팀에 Join 되어있는 플레이어가 죽었을 때는 아무 일도 일어나지 않고 Mushroom 팀에 Join 되어 있는 사람이 월드의 스폰으로 이동합니다.

## 승리 조건
- 버섯 : 모든 플레이어를 잡으면 승리
- 플레이어 : 아래 조합법으로 특별 아이템을 제작하면 승리
- 부활 신호기 (우클릭 시, 사용한 사람의 반경 10블록 이내에 있는 사망자를 부활시킴) (백신 레시피) 조합법

|   비어 있음   |   철 주괴    |   비어 있음   |
|:---------:|:---------:|:---------:|
| **에메랄드**  | **다이아몬드** | **구리 주괴** |
| **비어 있음** | **금 주괴**  | **비어 있음** |

- 포자 퇴치기 (우클릭 시, 포자 바이러스를 퇴치함) 조합법

|   갈색 버섯   |    부활 신호기    |   빨간 버섯   |
|:---------:|:------------:|:---------:|
|  **삼지창**  |   **유리병**    |  **삼지창**  |
| **황금 사과** | **네더라이트 주괴** | **황금 당근** |

## 안내
- 이 플러그인은 [GNU General Public License v3.0 라이선스](https://www.gnu.org/licenses/gpl-3.0.html)를 사용하며, **플러그인을 수정하여 사용할 시, 원작자 이름 표기가 필요합니다.** 또, **모든 수정된 플러그인은 [GNU General Public License v3.0 라이선스](https://www.gnu.org/licenses/gpl-3.0.html)를 반드시 사용해야 합니다.**
- 오류 제보는 [GitHub Issues](https://github.com/yj0524/PoisonousMushroom/issues)에 제보해주시고, 코드 수정 요청은 [GitHub Pull Request](https://github.com/yj0524/PoisonousMushroom/pulls)에 요청해주시기 바랍니다.

## 추가 사이트
- [포자 : 최후의 생존자들 - 컨텐츠 룰](https://poisonousmushroomrule.mushtle.co.kr)

## Contact Email
- admin@mushtle.co.kr

## Special Thanks
<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" width="256" height="256"/>

- Open Source License Support by JetBrains
