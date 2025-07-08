import json

# 입력 파일과 출력 파일 이름 설정
input_filename = 'item.json'
output_filename = 'item_unique_by_id.json'

def remove_duplicates_by_smaller_id():
    """
    item.json 파일을 읽어 아이템 이름 기준으로 중복을 제거하되,
    ID가 더 작은 아이템을 우선하여 저장하고 새 JSON 파일을 생성합니다.
    """
    try:
        # 1. 원본 JSON 파일 읽기
        with open(input_filename, 'r', encoding='utf-8') as f:
            full_data = json.load(f)

    except FileNotFoundError:
        print(f"오류: '{input_filename}' 파일을 찾을 수 없습니다.")
        return
    except json.JSONDecodeError:
        print(f"오류: '{input_filename}' 파일이 올바른 JSON 형식이 아닙니다.")
        return

    # 2. 중복 제거 로직 수행
    original_item_data = full_data.get('data', {})
    
    # 아이템 이름을 키로, {id, details}를 값으로 저장할 딕셔너리
    # 각 이름에 대해 ID가 가장 작은 아이템 정보만 남기기 위함
    best_items_map = {} 

    print("중복 제거 작업을 시작합니다 (ID가 작은 아이템 우선)...")

    for item_id, item_details in original_item_data.items():
        item_name = item_details.get('name', '').strip()
        
        # 이름이 없는 아이템은 무시하거나 고유하게 처리할 수 있음 (여기선 고유하게 처리)
        if not item_name:
            # 이름 없는 아이템은 ID 충돌이 없으므로 그대로 추가
            if item_id not in best_items_map:
                 best_items_map[item_id] = {'id': item_id, 'details': item_details}
            continue

        # 현재 아이템 ID를 정수로 변환
        current_id_int = int(item_id)

        # 처음 보는 이름인 경우, 바로 등록
        if item_name not in best_items_map:
            best_items_map[item_name] = {'id': item_id, 'details': item_details}
        else:
            # 이미 등록된 이름인 경우, ID 비교
            existing_item = best_items_map[item_name]
            existing_id_int = int(existing_item['id'])
            
            # 현재 아이템의 ID가 기존 아이템의 ID보다 작으면 교체
            if current_id_int < existing_id_int:
                best_items_map[item_name] = {'id': item_id, 'details': item_details}

    # 3. 최종 데이터 구조 생성
    # best_items_map에 저장된 "승자"들만으로 새로운 data 객체를 만듦
    unique_items_data = {}
    for item in best_items_map.values():
        unique_items_data[item['id']] = item['details']
        
    original_count = len(original_item_data)
    unique_count = len(unique_items_data)

    # 원본 구조를 유지하되 'data' 부분만 교체
    full_data['data'] = unique_items_data

    # 4. 중복 제거된 데이터를 새 파일에 저장
    with open(output_filename, 'w', encoding='utf-8') as f:
        json.dump(full_data, f, indent=4, ensure_ascii=False)

    print("\n✅ 작업 완료!")
    print(f"원본 아이템 개수: {original_count}")
    print(f"고유 아이템 개수: {unique_count}")
    print(f"제거된 중복 아이템 개수: {original_count - unique_count}")
    print(f"결과가 '{output_filename}' 파일에 저장되었습니다.")


# 스크립트 실행
if __name__ == "__main__":
    remove_duplicates_by_smaller_id()