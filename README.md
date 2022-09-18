# BetterCinematics
마인크래프트에서 지정한 장면 순서대로 자연스럽게 화면이 연출되는 시네마틱 플러그인입니다.

---

# Commands
> **Permission**: `bettercinematics.command.%command%`   
> **Aliases**: /bc, /bcm, /bcinematics  
> **Arguments-Type**: \<primary>, \(choice), \[optional]
- /bc create \<name> - 새로운 시네마틱을 생성합니다.
- /bc delete \<name> - 시네마틱을 삭제합니다.
- /bc list \[page] - 시네마틱 목록을 확인합니다. 
- /bc edit \<name> - 시네마틱의 위치 설정을 시작합니다.
  - /bc pos set \(from/to) - 시작 또는 끝 위치를 지정합니다.
  - /bc scene add \[index] - 지정한 두 위치를 하나의 구간으로 추가합니다.
  - /bc scene remove \<index> - 해당 구간을 삭제합니다.
  - /bc scene list - 추가된 구간 목록을 확인합니다.
  - /bc scene speed \<index> \<speed> - 해당 구간의 속도를 설정합니다.
  - /bc ease \(basic/bezier) - 부드러운 곡선으로 연출합니다.
  - /bc mode \(primitive/better/super) - 시네마틱 방식을 설정합니다.
  - /bc angle \[\(true/false)] - 화면 고정 여부를 설정합니다.
- /bc tp \<scene-index> \[\(from/to)] - 해당 위치로 이동합니다.
- /bc preview \<name> \(all/pos/dir/line/curve) - 이동 동선을 확인합니다.
- /bc play \<name> \<duration> \[frame-interval] \[player] - 해당 시간동안 시네마틱을 재생합니다.
- /bc stop \[player] - 현재 재생 중인 시네마틱을 중지합니다.