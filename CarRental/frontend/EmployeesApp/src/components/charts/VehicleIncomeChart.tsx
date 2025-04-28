import { Chart } from 'primereact/chart';
import { ChartLayout } from './ChartLayout';
import { VehicleTypeIncome } from 'types/types';

interface MonthlyIncomeChartProps {
    data: VehicleTypeIncome[];
}

const VehicleIncomeChart = ({ data }: MonthlyIncomeChartProps) => {
    // Extract labels and values from the data
    const labels = data.map(item => item.vehicleType);
    const incomeValues = data.map(item => item.income);

    // Define colors for each vehicle type
    const backgroundColors = [
        'rgba(255, 159, 64, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(153, 102, 255, 0.2)'
    ];
    
    const borderColors = [
        'rgb(255, 159, 64)',
        'rgb(75, 192, 192)',
        'rgb(54, 162, 235)',
        'rgb(153, 102, 255)'
    ];
    
    const chartData = {
        labels: labels, // Dynamic labels from data
        datasets: [
            {
                label: 'Income by Vehicle Type',
                data: incomeValues, // Just the numbers
                backgroundColor: backgroundColors.slice(0, data.length), // Only use needed colors
                borderColor: borderColors.slice(0, data.length),
                borderWidth: 1
            }
        ]
    };

    const chart = <Chart type="bar" data={chartData} />;

    return (
        <ChartLayout chart={chart} />
    );
};

export default VehicleIncomeChart;