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
    mediaAppsCnt: number;
    hourlyInterestedCount: number[];
    hourlyPassedCount: number[];
    hourlyAvgStaringTime: number[];
    totalPeopleCount: number;
    avgStaringTime: number;
    avgAge: number;
    maleInterestCnt: number;
    femaleInterestCnt: number;
    maleCnt: number;
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