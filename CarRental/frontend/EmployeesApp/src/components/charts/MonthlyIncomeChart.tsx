
import { Chart } from 'primereact/chart';
import { Dropdown } from 'primereact/dropdown';
import { useState } from 'react';
import { ChartLayout } from './ChartLayout';
import { MonthlyIncome } from 'types/types';

interface MonthlyIncomeChartProps {
    data: MonthlyIncome[];
}

interface Month {
    name: string;
    index: number;
}

const MonthlyIncomeChart = ({ data }: MonthlyIncomeChartProps) => {
    const [month, setMonth] = useState<Month>({ name: 'January', index: 0 });
    const months: Month[] = [
        { name: 'January', index: 0 },
        { name: 'February', index: 1 },
        { name: 'March', index: 2 },
        { name: 'April', index: 3 },
        { name: 'May', index: 4 },
        { name: 'June', index: 5 },
        { name: 'July', index: 6 },
        { name: 'August', index: 7 },
        { name: 'September', index: 8 },
        { name: 'October', index: 9 },
        { name: 'November', index: 10 },
        { name: 'December', index: 11 }
    ];

    // Filter and transform data for the selected month
    const getChartDataForMonth = () => {
        const currentYear = new Date().getFullYear();
        const monthlyData = data.filter(item => {
            const date = new Date(item.date); // Convert string to Date
            return (
                date.getMonth() === month.index && 
                date.getFullYear() === currentYear
            );
        });
        return monthlyData.map(item => item.income); // No need for Number() since income is already number
    };

    // Function to get the number of days in a selected month
    const getDaysInMonth = (monthIndex: number, year = new Date().getFullYear()) => {
        return new Date(year, monthIndex + 1, 0).getDate();
    };

    const daysInMonth = getDaysInMonth(month.index);
    const labels = Array.from({ length: daysInMonth }, (_, i) => i + 1);
    const incomeData = getChartDataForMonth();

    const chartData = {
        labels: labels,
        datasets: [
            {
                data: incomeData,
                backgroundColor: 'rgba(13, 131, 211, 0.4)',
                hoverBackgroundColor: ['#0097e6', '#00a8ff'],
                label: `${month.name}`,
                fill: true,
                tension: 0.4,
            }
        ]
    };

    const filters = <Dropdown 
                        value={month} 
                        options={months} 
                        optionLabel="name" 
                        onChange={(e) => setMonth(e.value)}
                        placeholder='Month' 
                        checkmark={true}
                        highlightOnSelect={true}
                    />;
    const chart = <Chart type="line" data={chartData} />;

    return (
        <ChartLayout chart={chart} filters={filters}  />
    );
};

export default MonthlyIncomeChart;