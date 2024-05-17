import privateApi from "../private_api"

export interface CreateDailyDashboardProps {
    dashboardId: number;
    mediaApplicationId: number;
    date: string; // "YYYY-MM-DD"
}

export interface CreateLocationDashboardProps {
    locationId: number;
}

export interface CompareDashboardsProps {
    applyId: number[];
}

export interface GetAdUnitDashboardProps {
    dashboardId: number;
}

export interface GetDashboardListByAdUnitProps {
    dashboardId: number;
}

// 날짜 별(광고 + 집행기간 + 일(day)) 단위 대시보드 생성
export const createDailyDashboard = async ({dashboardId, mediaApplicationId, date}: CreateDailyDashboardProps) => {
    const response = await privateApi.post(`/api/v1/dashboard/${dashboardId}/mediaApplication/${mediaApplicationId}`, {date}).catch((err) => {return err.response});
    return response;
}

// Location(디스플레이) 단위 대시보드 생성
export const createLocationDashboard = async ({locationId}: CreateLocationDashboardProps) => {
    const response = await privateApi.post(`/api/v1/dashboard/location/${locationId}`).catch((err) => {return err.response});
    return response;
}

// 대시보드 비교 
export const compareDashboards = async ({applyId}: CompareDashboardsProps) => {
    const response = await privateApi.post('/api/v1/dashboard/compare', {applyId}).catch((err) => {return err.response});
    return response;
}

// 광고 단위별 대시보드
export const getAdUnitDashboard = async ({dashboardId}: GetAdUnitDashboardProps) => {
    const response = await privateApi.get(`/api/v1/dashboard/${dashboardId}`).catch((err) => {return err.response});
    return response;
}

// 신청 단위(광고 + 집행 시간) 별 대시보드 리스트
export const getDashboardListByAdUnit = async ({dashboardId}: GetDashboardListByAdUnitProps) => {
    const response = await privateApi.get(`/api/v1/dashboard/${dashboardId}/board`).catch((err) => {return err.response});
    return response;
}
