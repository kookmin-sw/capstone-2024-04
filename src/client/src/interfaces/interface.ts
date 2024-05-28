/** API Interface */
export interface SignUpAPIProps {
    email: string;
    password: string;
    company: string;
}

export interface SignInAPIProps {
    email: string;
    password: string;
}

export interface ErrorResponse {
    status: number,
    divisionCode: string,
    resultMsg: string,
    errors: {},
    reason: string,
}

export interface LocationInfo {
    locationId: number;
    address: string;
}

export interface UserInfo {
    userId: number;
    email: string;
    company: string;
    profileImage: string;
}

export interface TodayList {
    playListId: number;
    posting: boolean;
    location: Location;
    media: MediaInfo;
}

export interface MediaApplicationInfo {
    applicationId: number;
    startDate: string;
    endDate: string;
    status: string;
    location: LocationInfo;
    user: UserInfo;
}

export interface TotalApplicationInfo {
    media: MediaInfo;
    application: MediaApplicationInfo;
}


/** ETC */
export interface SignUpPageProps {
    goToSignIn: any;
}
export interface SignInPageProps {
    goToSignUp: any;
    goToFindPassword: any;
  }

export interface FindPasswordPageProps {
    goToSignIn: any;
}

export interface datePerDay {
    date: string;
}

export interface DashboardDataInfo {
    mediaAppsCnt: number | null; // 해당 광고에 집계된, 집행된 광고의 날짜수
    interestedPeopleAgeRangeCount: number[] | null;
    hourlyInterestedCount: number[] | null; // 시간당 관심을 표현한 사람 수(0시 ~ 23시)
    hourlyPassedCount: number[] | null; // 시간당 포착된 사람 수 (0시 ~ 23시)
    hourlyAvgStaringTime: number[] | null; // 시간당 평균 응시 횟수 (0시 ~ 23시)
    totalPeopleCount: number | null; // 전체 포착된 사람 수 
    avgStaringTime: number | null; // 해당 광고 평균 시청 시간
    avgAge: number | null; // 해당 광고 평균 시청 나이 
    maleInterestCnt: number | null; // 관심을 표현한 남자 인원 수
    femaleInterestCnt: number | null; // 관심을 표현한 여자의 인원수
    maleCnt: number | null; // 집계된 남자의 인원 수
}

export interface LocationDataInfo{
    mediaAppsCnt: number;
    passedPeopleCntPerDay: number;
    passedPeopleListPerHour: number[];
    totalAgeRangeCount: number[];
    avgMaleRatio: number;
}



export interface MenuButtonProps {
    title: string;
    iconWhiteSrc: string;
    iconBlackSrc: string;
    isActive: boolean;
    onClick: React.MouseEventHandler<HTMLDivElement>;
}

export interface ContentProps {
    label: JSX.Element;
    value: JSX.Element;
}

export interface Option {
    value: string | number;
    label: string;
    children?: Option[];
}

export interface MediaInfo {
    mediaId: number,
    mediaLink: string,
    title: string,
    description: string,
    dashboardId: number,
}

export interface DashBoardInfo {
    title: string,
    description: string,
}

export interface Create {
    advertisementTitle: string,
    advertisementDescription: string,
    locationId: number,
    startDate: string,
    endDate: string,
}

export interface MediaApplicationList {
    applyId: number[],
}