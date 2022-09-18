# BetterCinematics
마인크래프트에서 지정한 장면 순서대로 자연스럽게 화면이 연출되는 시네마틱 플러그인입니다.

---

# Commands
> **Permission**: `bettercinematics.command.%command%`   
> **Aliases**: /bc, /bcm, /bcinematics 
- /bc create \<name> - 새로운 시네마틱을 생성합니다.
- /bc delete \<name> - 시네마틱을 삭제합니다.
- /bc list \(page) - 시네마틱 목록을 확인합니다. 
- /bc pos - 위치 관련 명령어를 확인합니다.
  - /bc pos add \<name> - 현재 위치를 추가합니다.
  - /bc pos remove \<name> \<index> - 해당 위치를 삭제합니다.
  - /bc pos list \<name> - 추가된 위치 목록을 확인합니다.
  - /bc pos teleport \<name> \<index> - 해당 위치로 이동합니다.
  - /bc pos speed \<name> \<from> \<to> \<speed> - 인접한 두 지점 사이의 속도를 설정합니다.
  - /bc pos ease \<name> \(basic/bezier) - 부드러운 곡선으로 연출합니다.
- /bc preview \<name> \(all/pos/dir/line/curve) - 이동 동선을 확인합니다.
- /bc mode \<name> \(primitive/better/super) - 시네마틱 방식을 설정합니다.
- /bc angle \<name> \[true/false] - 화면 고정 여부를 설정합니다.
- /bc play \<name> \<duration> \[player] - 해당 시간동안 시네마틱을 재생합니다.
- /bc stop \[player] - 현재 재생 중인 시네마틱을 중지합니다.