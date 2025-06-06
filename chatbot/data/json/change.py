import re
import json

def decode_contents(text):
    # 'clobXXX:' 제거하고 앞뒤 공백 제거
    if ':' in text:
        return text.split(':', 1)[1].strip()
    return text

def decode_unicode_escapes(s):
    # "U&'" 포함된 문자열 처리
    if "U&'" in s:
        start = s.find("U&'") + 3
        end = s.rfind("'")
        unicode_str = s[start:end]

        # \XXXX 형태만 \uXXXX로 변환 (역슬래시 + 4자리 16진수)
        unicode_str = re.sub(r'\\([0-9A-Fa-f]{4})', r'\\u\1', unicode_str)

        try:
            # 유니코드 이스케이프 디코딩
            decoded = unicode_str.encode().decode('unicode_escape')
            return decoded
        except Exception as e:
            # 실패하면 원본 문자열 반환
            return s
    else:
        return s

# 예시 데이터
data = [

        {
            "SCHEDULE_ID": 1,
            "MONTHVALUE": "3월 1일",
            "CONTENTS": "clob0: 학기개시일, 삼일절"
        },
        {
            "SCHEDULE_ID": 2,
            "MONTHVALUE": "3월 4일",
            "CONTENTS": "clob1: 개강일"
        },
        {
            "SCHEDULE_ID": 3,
            "MONTHVALUE": "3월 6 ~ 10일",
            "CONTENTS": "clob2: 수강정정"
        },
        {
            "SCHEDULE_ID": 4,
            "MONTHVALUE": "3월 27일",
            "CONTENTS": "clob3: 수업일수 1/4선"
        },
        {
            "SCHEDULE_ID": 5,
            "MONTHVALUE": "3월 30일",
            "CONTENTS": "clob4: 학기개시일 30일째"
        },
        {
            "SCHEDULE_ID": 6,
            "MONTHVALUE": "4월 7일",
            "CONTENTS": "clob5: 수업일수 1/3선"
        },
        {
            "SCHEDULE_ID": 7,
            "MONTHVALUE": "4월 22 ~ 28일",
            "CONTENTS": "clob6: 중간시험"
        },
        {
            "SCHEDULE_ID": 8,
            "MONTHVALUE": "4월 23일",
            "CONTENTS": "clob7: 수업일수 1/2선"
        },
        {
            "SCHEDULE_ID": 9,
            "MONTHVALUE": "4월 29일",
            "CONTENTS": "clob8: 학기경과일수 60일째"
        },
        {
            "SCHEDULE_ID": 10,
            "MONTHVALUE": "5월 1일",
            "CONTENTS": "clob9: 근로자의 날"
        },
        {
            "SCHEDULE_ID": 11,
            "MONTHVALUE": "5월 5일",
            "CONTENTS": "clob10: 어린이날, 부처님오신날"
        },
        {
            "SCHEDULE_ID": 12,
            "MONTHVALUE": "5월 6일",
            "CONTENTS": "clob11: 대체휴일(어린이날)"
        },
        {
            "SCHEDULE_ID": 13,
            "MONTHVALUE": "5월 15일",
            "CONTENTS": "clob12: 수업일수 2/3선"
        },
        {
            "SCHEDULE_ID": 14,
            "MONTHVALUE": "5월 23일",
            "CONTENTS": "clob13: 수업일수 3/4선"
        },
        {
            "SCHEDULE_ID": 15,
            "MONTHVALUE": "5월 29일",
            "CONTENTS": "clob14: 학기경과일수 90일째"
        },
        {
            "SCHEDULE_ID": 16,
            "MONTHVALUE": "6월 3일",
            "CONTENTS": "clob15: 대통령 선거일"
        },
        {
            "SCHEDULE_ID": 17,
            "MONTHVALUE": "6월 6일",
            "CONTENTS": "clob16: 현충일"
        },
        {
            "SCHEDULE_ID": 18,
            "MONTHVALUE": "6월 10일",
            "CONTENTS": "clob17: 지정보강일(5.6 대체휴일-어린이날)"
        },
        {
            "SCHEDULE_ID": 19,
            "MONTHVALUE": "6월 11일",
            "CONTENTS": "clob18: 지정보강일(5.5 어린이날)"
        },
        {
            "SCHEDULE_ID": 20,
            "MONTHVALUE": "6월 12일",
            "CONTENTS": "clob19: 지정보강일(5.1 근로자의날)"
        },
        {
            "SCHEDULE_ID": 21,
            "MONTHVALUE": "6월 13일",
            "CONTENTS": "clob20: 지정보강일(6.6 현충일)"
        },
        {
            "SCHEDULE_ID": 22,
            "MONTHVALUE": "6월 16일",
            "CONTENTS": "clob21: 지정보강일(6.3 대통령선거일)"
        },
        {
            "SCHEDULE_ID": 23,
            "MONTHVALUE": "6월 17 ~ 23일",
            "CONTENTS": "clob22: 기말시험"
        },
        {
            "SCHEDULE_ID": 24,
            "MONTHVALUE": "6월 24일",
            "CONTENTS": "clob23: 하계방학 시작"
        },
        {
            "SCHEDULE_ID": 25,
            "MONTHVALUE": "6월 24일 ~ 7월 14일",
            "CONTENTS": "clob24: 하계계절수업"
        },
        {
            "SCHEDULE_ID": 26,
            "MONTHVALUE": "7월 6월24일 ~ 14일",
            "CONTENTS": "clob25: 하계계절수업"
        },
        {
            "SCHEDULE_ID": 27,
            "MONTHVALUE": "8월 15일",
            "CONTENTS": "clob26: 광복절"
        },
        {
            "SCHEDULE_ID": 28,
            "MONTHVALUE": "8월 18 ~ 22일",
            "CONTENTS": "clob27: 수강신청"
        },
        {
            "SCHEDULE_ID": 29,
            "MONTHVALUE": "8월 21 ~ 26일",
            "CONTENTS": "clob28: 현금등록"
        },
        {
            "SCHEDULE_ID": 30,
            "MONTHVALUE": "8월 22일",
            "CONTENTS": "clob29: 2024학년도 후기 학위 수여식"
        },
        {
            "SCHEDULE_ID": 31,
            "MONTHVALUE": "9월 1일",
            "CONTENTS": "clob30: 학기개시일, 개강일"
        },
        {
            "SCHEDULE_ID": 32,
            "MONTHVALUE": "9월 3 ~ 5일",
            "CONTENTS": "clob31: 수강정정"
        },
        {
            "SCHEDULE_ID": 33,
            "MONTHVALUE": "9월 24일",
            "CONTENTS": "clob32: 수업일수 1/4선"
        },
        {
            "SCHEDULE_ID": 34,
            "MONTHVALUE": "9월 30일",
            "CONTENTS": "clob33: 학기경과일수 30일째"
        },
        {
            "SCHEDULE_ID": 35,
            "MONTHVALUE": "10월 3일",
            "CONTENTS": "clob34: 개천절"
        },
        {
            "SCHEDULE_ID": 36,
            "MONTHVALUE": "10월 5 ~ 7일",
            "CONTENTS": "clob35: 추석"
        },
        {
            "SCHEDULE_ID": 37,
            "MONTHVALUE": "10월 8일",
            "CONTENTS": "clob36: 대체휴일(추석)"
        },
        {
            "SCHEDULE_ID": 38,
            "MONTHVALUE": "10월 9일",
            "CONTENTS": "clob37: 한글날"
        },
        {
            "SCHEDULE_ID": 39,
            "MONTHVALUE": "10월 10일",
            "CONTENTS": "clob38: 수업일수 1/3선"
        },
        {
            "SCHEDULE_ID": 40,
            "MONTHVALUE": "10월 22일",
            "CONTENTS": "clob39: 개교기념일"
        },
        {
            "SCHEDULE_ID": 41,
            "MONTHVALUE": "10월 27 ~ 31일",
            "CONTENTS": "clob40: 중간시험"
        },
        {
            "SCHEDULE_ID": 42,
            "MONTHVALUE": "10월 29일",
            "CONTENTS": "clob41: 수업일수 1/2선"
        },
        {
            "SCHEDULE_ID": 43,
            "MONTHVALUE": "10월 30일",
            "CONTENTS": "clob42: 학기경과일수 60일째"
        },
        {
            "SCHEDULE_ID": 44,
            "MONTHVALUE": "11월 17일",
            "CONTENTS": "clob43: 수업일수 2/3선"
        },
        {
            "SCHEDULE_ID": 45,
            "MONTHVALUE": "11월 25일",
            "CONTENTS": "clob44: 수업일수 3/4선"
        },
        {
            "SCHEDULE_ID": 46,
            "MONTHVALUE": "11월 29일",
            "CONTENTS": "clob45: 학기경과일수 90일째"
        },
        {
            "SCHEDULE_ID": 47,
            "MONTHVALUE": "12월 8 ~ 9일",
            "CONTENTS": "clob46: 지정보강일(10.6/10.7 추석)"
        },
        {
            "SCHEDULE_ID": 48,
            "MONTHVALUE": "12월 10일",
            "CONTENTS": "clob47: 지정보강일(10.8 대체공휴일-추석)"
        },
        {
            "SCHEDULE_ID": 49,
            "MONTHVALUE": "12월 11일",
            "CONTENTS": "clob48: 지정보강일(10.9 한글날)"
        },
        {
            "SCHEDULE_ID": 50,
            "MONTHVALUE": "12월 12일",
            "CONTENTS": "clob49: 지정보강일(10.3 개천절)"
        },
        {
            "SCHEDULE_ID": 51,
            "MONTHVALUE": "12월 15일",
            "CONTENTS": "clob50: 지정보강일(10.22 개교기념일)"
        },
        {
            "SCHEDULE_ID": 52,
            "MONTHVALUE": "12월 16 ~ 22일",
            "CONTENTS": "clob51: 기말시험"
        },
        {
            "SCHEDULE_ID": 53,
            "MONTHVALUE": "12월 23일",
            "CONTENTS": "clob52: 동계방학 시작일"
        },
        {
            "SCHEDULE_ID": 54,
            "MONTHVALUE": "12월 25일",
            "CONTENTS": "clob53: 성탄절"
        },
        {
            "SCHEDULE_ID": 55,
            "MONTHVALUE": "12월 29일 ~ 1월 19일",
            "CONTENTS": "clob54: 동계계절수업"
        },
        {
            "SCHEDULE_ID": 56,
            "MONTHVALUE": "2026년 1월 12월29일 ~ 19일",
            "CONTENTS": "clob55: 동계계절수업"
        },
        {
            "SCHEDULE_ID": 57,
            "MONTHVALUE": "2026년 1월 1일",
            "CONTENTS": "clob56: 신정"
        },
        {
            "SCHEDULE_ID": 58,
            "MONTHVALUE": "2026년 2월 16 ~ 18일",
            "CONTENTS": "clob57: 설날"
        },
        {
            "SCHEDULE_ID": 59,
            "MONTHVALUE": "2026년 2월 19 ~ 24일",
            "CONTENTS": "clob58: 현금등록, 수강신청"
        },
        {
            "SCHEDULE_ID": 60,
            "MONTHVALUE": "2026년 2월 20일",
            "CONTENTS": "clob59: 2025학년도 전기 학위 수여식"
        },
        {
            "SCHEDULE_ID": 61,
            "MONTHVALUE": "2025년 1학기 3월 1일",
            "CONTENTS": "clob60: 학기개시일, 삼일절"
        },
        {
            "SCHEDULE_ID": 62,
            "MONTHVALUE": "2025년 1학기 3월 4일",
            "CONTENTS": "clob61: 개강일"
        },
        {
            "SCHEDULE_ID": 63,
            "MONTHVALUE": "2025년 1학기 3월 6 ~ 10일",
            "CONTENTS": "clob62: 수강정정"
        },
        {
            "SCHEDULE_ID": 64,
            "MONTHVALUE": "2025년 1학기 3월 27일",
            "CONTENTS": "clob63: 수업일수 1/4선"
        },
        {
            "SCHEDULE_ID": 65,
            "MONTHVALUE": "2025년 1학기 3월 30일",
            "CONTENTS": "clob64: 학기개시일 30일째"
        },
        {
            "SCHEDULE_ID": 66,
            "MONTHVALUE": "2025년 1학기 4월 7일",
            "CONTENTS": "clob65: 수업일수 1/3선"
        },
        {
            "SCHEDULE_ID": 67,
            "MONTHVALUE": "2025년 1학기 4월 22 ~ 28일",
            "CONTENTS": "clob66: 중간시험"
        },
        {
            "SCHEDULE_ID": 68,
            "MONTHVALUE": "2025년 1학기 4월 23일",
            "CONTENTS": "clob67: 수업일수 1/2선"
        },
        {
            "SCHEDULE_ID": 69,
            "MONTHVALUE": "2025년 1학기 4월 29일",
            "CONTENTS": "clob68: 학기경과일수 60일째"
        },
        {
            "SCHEDULE_ID": 70,
            "MONTHVALUE": "2025년 1학기 5월 1일",
            "CONTENTS": "clob69: 근로자의 날"
        },
        {
            "SCHEDULE_ID": 71,
            "MONTHVALUE": "2025년 1학기 5월 5일",
            "CONTENTS": "clob70: 어린이날, 부처님오신날"
        },
        {
            "SCHEDULE_ID": 72,
            "MONTHVALUE": "2025년 1학기 5월 6일",
            "CONTENTS": "clob71: 대체휴일(어린이날)"
        },
        {
            "SCHEDULE_ID": 73,
            "MONTHVALUE": "2025년 1학기 5월 15일",
            "CONTENTS": "clob72: 수업일수 2/3선"
        },
        {
            "SCHEDULE_ID": 74,
            "MONTHVALUE": "2025년 1학기 5월 23일",
            "CONTENTS": "clob73: 수업일수 3/4선"
        },
        {
            "SCHEDULE_ID": 75,
            "MONTHVALUE": "2025년 1학기 5월 29일",
            "CONTENTS": "clob74: 학기경과일수 90일째"
        },
        {
            "SCHEDULE_ID": 76,
            "MONTHVALUE": "2025년 1학기 6월 3일",
            "CONTENTS": "clob75: 대통령 선거일"
        },
        {
            "SCHEDULE_ID": 77,
            "MONTHVALUE": "2025년 1학기 6월 6일",
            "CONTENTS": "clob76: 현충일"
        },
        {
            "SCHEDULE_ID": 78,
            "MONTHVALUE": "2025년 1학기 6월 10일",
            "CONTENTS": "clob77: 지정보강일(5.6 대체휴일-어린이날)"
        },
        {
            "SCHEDULE_ID": 79,
            "MONTHVALUE": "2025년 1학기 6월 11일",
            "CONTENTS": "clob78: 지정보강일(5.5 어린이날)"
        },
        {
            "SCHEDULE_ID": 80,
            "MONTHVALUE": "2025년 1학기 6월 12일",
            "CONTENTS": "clob79: 지정보강일(5.1 근로자의날)"
        },
        {
            "SCHEDULE_ID": 81,
            "MONTHVALUE": "2025년 1학기 6월 13일",
            "CONTENTS": "clob80: 지정보강일(6.6 현충일)"
        },
        {
            "SCHEDULE_ID": 82,
            "MONTHVALUE": "2025년 1학기 6월 16일",
            "CONTENTS": "clob81: 지정보강일(6.3 대통령선거일)"
        },
        {
            "SCHEDULE_ID": 83,
            "MONTHVALUE": "2025년 1학기 6월 17 ~ 23일",
            "CONTENTS": "clob82: 기말시험"
        },
        {
            "SCHEDULE_ID": 84,
            "MONTHVALUE": "2025년 1학기 6월 24일",
            "CONTENTS": "clob83: 하계방학 시작"
        },
        {
            "SCHEDULE_ID": 85,
            "MONTHVALUE": "2025년 1학기 6월 24일 ~ 7월 14일",
            "CONTENTS": "clob84: 하계계절수업"
        },
        {
            "SCHEDULE_ID": 86,
            "MONTHVALUE": "2025년 1학기 8월 15일",
            "CONTENTS": "clob85: 광복절"
        },
        {
            "SCHEDULE_ID": 87,
            "MONTHVALUE": "2025년 1학기 8월 18 ~ 22일",
            "CONTENTS": "clob86: 수강신청"
        },
        {
            "SCHEDULE_ID": 88,
            "MONTHVALUE": "2025년 1학기 8월 21 ~ 26일",
            "CONTENTS": "clob87: 현금등록"
        },
        {
            "SCHEDULE_ID": 89,
            "MONTHVALUE": "2025년 1학기 8월 22일",
            "CONTENTS": "clob88: 2024학년도 후기 학위 수여식"
        },
        {
            "SCHEDULE_ID": 90,
            "MONTHVALUE": "2025년 2학기 9월 1일",
            "CONTENTS": "clob89: 학기개시일, 개강일"
        },
        {
            "SCHEDULE_ID": 91,
            "MONTHVALUE": "2025년 2학기 9월 3 ~ 5일",
            "CONTENTS": "clob90: 수강정정"
        },
        {
            "SCHEDULE_ID": 92,
            "MONTHVALUE": "2025년 2학기 9월 24일",
            "CONTENTS": "clob91: 수업일수 1/4선"
        },
        {
            "SCHEDULE_ID": 93,
            "MONTHVALUE": "2025년 2학기 9월 30일",
            "CONTENTS": "clob92: 학기경과일수 30일째"
        },
        {
            "SCHEDULE_ID": 94,
            "MONTHVALUE": "2025년 2학기 10월 3일",
            "CONTENTS": "clob93: 개천절"
        },
        {
            "SCHEDULE_ID": 95,
            "MONTHVALUE": "2025년 2학기 10월 5 ~ 7일",
            "CONTENTS": "clob94: 추석"
        },
        {
            "SCHEDULE_ID": 96,
            "MONTHVALUE": "2025년 2학기 10월 8일",
            "CONTENTS": "clob95: 대체휴일(추석)"
        },
        {
            "SCHEDULE_ID": 97,
            "MONTHVALUE": "2025년 2학기 10월 9일",
            "CONTENTS": "clob96: 한글날"
        },
        {
            "SCHEDULE_ID": 98,
            "MONTHVALUE": "2025년 2학기 10월 10일",
            "CONTENTS": "clob97: 수업일수 1/3선"
        },
        {
            "SCHEDULE_ID": 99,
            "MONTHVALUE": "2025년 2학기 10월 22일",
            "CONTENTS": "clob98: 개교기념일"
        },
        {
            "SCHEDULE_ID": 100,
            "MONTHVALUE": "2025년 2학기 10월 27 ~ 31일",
            "CONTENTS": "clob99: 중간시험"
        },
        {
            "SCHEDULE_ID": 101,
            "MONTHVALUE": "2025년 2학기 10월 29일",
            "CONTENTS": "clob100: 수업일수 1/2선"
        },
        {
            "SCHEDULE_ID": 102,
            "MONTHVALUE": "2025년 2학기 10월 30일",
            "CONTENTS": "clob101: 학기경과일수 60일째"
        },
        {
            "SCHEDULE_ID": 103,
            "MONTHVALUE": "2025년 2학기 11월 17일",
            "CONTENTS": "clob102: 수업일수 2/3선"
        },
        {
            "SCHEDULE_ID": 104,
            "MONTHVALUE": "2025년 2학기 11월 25일",
            "CONTENTS": "clob103: 수업일수 3/4선"
        },
        {
            "SCHEDULE_ID": 105,
            "MONTHVALUE": "2025년 2학기 11월 29일",
            "CONTENTS": "clob104: 학기경과일수 90일째"
        },
        {
            "SCHEDULE_ID": 106,
            "MONTHVALUE": "2025년 2학기 12월 8 ~ 9일",
            "CONTENTS": "clob105: 지정보강일(10.6/10.7 추석)"
        },
        {
            "SCHEDULE_ID": 107,
            "MONTHVALUE": "2025년 2학기 12월 10일",
            "CONTENTS": "clob106: 지정보강일(10.8 대체공휴일-추석)"
        },
        {
            "SCHEDULE_ID": 108,
            "MONTHVALUE": "2025년 2학기 12월 11일",
            "CONTENTS": "clob107: 지정보강일(10.9 한글날)"
        },
        {
            "SCHEDULE_ID": 109,
            "MONTHVALUE": "2025년 2학기 12월 12일",
            "CONTENTS": "clob108: 지정보강일(10.3 개천절)"
        },
        {
            "SCHEDULE_ID": 110,
            "MONTHVALUE": "2025년 2학기 12월 15일",
            "CONTENTS": "clob109: 지정보강일(10.22 개교기념일)"
        },
        {
            "SCHEDULE_ID": 111,
            "MONTHVALUE": "2025년 2학기 12월 16 ~ 22일",
            "CONTENTS": "clob110: 기말시험"
        },
        {
            "SCHEDULE_ID": 112,
            "MONTHVALUE": "2025년 2학기 12월 23일",
            "CONTENTS": "clob111: 동계방학 시작일"
        },
        {
            "SCHEDULE_ID": 113,
            "MONTHVALUE": "2025년 2학기 12월 25일",
            "CONTENTS": "clob112: 성탄절"
        },
        {
            "SCHEDULE_ID": 114,
            "MONTHVALUE": "2025년 2학기 12월 29일 ~ 1월 19일",
            "CONTENTS": "clob113: 동계계절수업"
        },
        {
            "SCHEDULE_ID": 115,
            "MONTHVALUE": "2025년 2학기 1월 1일",
            "CONTENTS": "clob114: 신정"
        },
        {
            "SCHEDULE_ID": 116,
            "MONTHVALUE": "2025년 2학기 2월 16 ~ 18일",
            "CONTENTS": "clob115: 설날"
        },
        {
            "SCHEDULE_ID": 117,
            "MONTHVALUE": "2025년 2학기 2월 19 ~ 24일",
            "CONTENTS": "clob116: 현금등록, 수강신청"
        },
        {
            "SCHEDULE_ID": 118,
            "MONTHVALUE": "2025년 2학기 2월 20일",
            "CONTENTS": "clob117: 2025학년도 전기 학위 수여식"
        }


]

# 1) clobXXX: 부분 제거
for item in data:
    item['CONTENTS'] = decode_contents(item['CONTENTS'])

# 2) 유니코드 이스케이프 디코딩
for item in data:
    item['CONTENTS'] = decode_unicode_escapes(item['CONTENTS'])

print(json.dumps(data, ensure_ascii=False, indent=2))
