
import { useEffect, useState } from "react";

import { getMalfunctionsByVehicleMap } from "@api/services/dashboardService";
import { ChartTitleContainer } from "@components/charts/ChartTitleContainer";
import MalfunctionPerVehicleChart from "@components/charts/MalfunctionPerVehicleChart";
import MonthlyIncomeChart from "@components/charts/MonthlyIncomeChart";
import VehicleIncomeChart from "@components/charts/VehicleIncomeChart";
import { getMonthlyIncomeData, getVehicleTypeIncomeData } from "@api/services/statisticsService";
import { MonthlyIncome, VehicleTypeIncome } from "types/types";

function StatisticsPage() {
    const [monthlyIncomeList, setMonthlyIncomeList] = useState<MonthlyIncome[]>([]);
    const [vehicleTypeIncomeList, setVehicleTypeIncomeList] = useState<VehicleTypeIncome[]>([]);
    const [vehicleMalfunctionsData, setVehicleMalfunctionsData] = useState<number[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            const monthlyIncomes = await getMonthlyIncomeData();
            const vehicleTypeIncomes = await getVehicleTypeIncomeData();
            const vehicleMalfunctionsMap = await getMalfunctionsByVehicleMap();
            
            setMonthlyIncomeList(monthlyIncomes);
            setVehicleTypeIncomeList(vehicleTypeIncomes);
            setVehicleMalfunctionsData(Object.keys(vehicleMalfunctionsMap).map(type => vehicleMalfunctionsMap[type]));
        }

        fetchData();
    }, []);


    const monthlyIncomeChart = <MonthlyIncomeChart data={monthlyIncomeList} />
    const vehicleMalfunctionsChart = <MalfunctionPerVehicleChart data={vehicleMalfunctionsData} />
    const vehicleIncomeChart = <VehicleIncomeChart data={vehicleTypeIncomeList} />

    return (
        <div className="statistics-container">
            <ChartTitleContainer title="MONTHLY INCOME" chart={monthlyIncomeChart} />
            <ChartTitleContainer title="VEHICLE MALFUNCTIONS" chart={vehicleMalfunctionsChart} />
            <ChartTitleContainer title="VEHICLE INCOME" chart={vehicleIncomeChart} />
        </div>
    );
}

export default StatisticsPage;