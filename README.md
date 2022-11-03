# 키친포스

## 미션 요구 사항

- [x] 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
    - 주문 생성 이후 메뉴의 정보를 변경하여도 주문 항목이 변경되지 않아야 한다. 주문은 menuId로 메뉴를 참조하지 않고 메뉴 데이터를 조회할 수 있어야한다.
    - `메뉴 이름`, `메뉴 가격`을 갖는 값 객체를 추가한다.
- [ ] 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.

## 요구 사항

### 상품

- [x] 상품을 등록할 수 있다.
    - [x] 상품은 이름과 가격을 갖는다.
    - [x] 가격은 비어있거나 음수일 수 없다.
- [x] 상품 목록을 조회할 수 있다.

### 메뉴 그룹

- [x] 메뉴 그룹을 등록할 수 있다.
    - [x] 이름을 갖는다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴

- [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴는 이름, 가격, 메뉴 그룹 ID, 여러 메뉴 상품을 갖는다.
    - [x] 메뉴 상품은 상품 ID와 수량을 갖는다.
        - [x] 시스템에 등록된 상품이어야 한다.
        - [x] 가격은 각 메뉴 상품의 상품 가격과 수량을 곱한 값(금액)의 총 합보다 클 수 없다. (e.g. 가격이 1000인 상품 1개와 가격이 2000인 상품 2개로 구성된 메뉴는 가격이 3000을 넘을 수
          없다.)
    - [x] 메뉴는 여러 개의 메뉴 상품을 가질 수 있다.
    - [x] 가격은 비어있거나 음수일 수 없다.
    - [x] 시스템에 등록된 메뉴 그룹이어야 한다.
- [x] 메뉴 목록을 조회할 수 있다.

### 주문 테이블

- [x] 주문 테이블을 등록할 수 있다.
    - [x] 주문 테이블은 방문한 손님 수와 주문 등록 가능 여부를 갖는다.
        - [x] 방문한 손님 수는 필수 사항이 아니므로 0일 수 있다.
- [x] 주문 테이블 목록을 조회할 수 있다.
- [x] 주문 등록 가능 여부를 변경할 수 있다.
    - [x] 주문 등록 가능 여부를 변경하려는 대상 주문 테이블은 시스템에 등록된 상태여야 한다.
    - [x] 단체 지정된 주문 테이블은 주문 등록 가능 여부를 변경할 수 없다.
    - [x] 주문 테이블 상태가 매장 식사인 경우 주문 등록 가능 여부를 변경할 수 없다.
- [x] 방문한 손님 수를 변경할 수 있다.
    - [x] 방문한 손님 수는 음수일 수 없다.
    - [x] 방문한 손님 수를 변경하려는 대상 주문 테이블은 시스템에 등록된 상태여야 한다.
    - [x] 주문 테이블 상태가 매장 식사인 경우 손님 수는 변경할 수 없다.

### 단체 지정

- [x] 단체 지정을 등록할 수 있다.
    - [x] 여러 주문 테이블 ID를 갖는다.
    - [x] 주문 테이블은 비어있거나 2개 미만일 수 없다.
    - [x] 모든 주문 테이블은 시스템에 등록된 상태여야 한다.
    - [x] 모든 주문 테이블은 빈 테이블이어야 한다.
    - [x] 이미 단체 지정된 주문 테이블이 하나라도 포함되면 단체 지정을 할 수 없다.
- [x] 단체 지정을 해제할 수 있다.
    - [x] 모든 주문 테이블에 주문이 있다면 주문 상태는 계산 완료여야 한다.

### 주문

- [x] 주문을 생성할 수 있다.
    - [x] 주문 테이블 ID와 여러 주문 항목을 갖는다.
    - [x] 주문 항목은 비어있거나 0개일 수 없다.
    - [x] 주문 항목은 메뉴 ID와 수량을 갖는다.
    - [x] 각각의 주문 항목의 메뉴 ID는 서로 중복되지 않아야 한다.
    - [x] 모든 메뉴는 시스템에 등록된 상태여야 한다.
    - [x] 주문 테이블은 시스템에 등록된 상태여야 한다.
    - [x] 주문 테이블은 빈 테이블일 수 없다.
- [x] 주문 목록들 조회할 수 있다.
- [x] 주문 상태를 변경할 수 있다.
    - [x] 주문 상태를 변경하려는 대상 주문은 시스템에 등록된 상태여야 한다.
    - [x] 주문 상태를 변경하려는 대상 주문의 주문 상태는 계산 완료일 수 없다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

## ERD

<p align="center">
    <img src="https://user-images.githubusercontent.com/68512686/197355193-94f6a7fb-9e1d-4bff-b2ab-f5a9b735a5fa.png">
</p>
