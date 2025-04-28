import { Chart } from 'primereact/chart';
import { ChartLayout } from './ChartLayout';

interface VehicleChartProps {
    data: number[];
}

const MalfunctionPerVehicleChart = ({ data }: VehicleChartProps) => {
    const chartData = {
        labels: ['Scooter', 'Car', 'Bicycle'],
        datasets: [
            {
                data: data, // dynamic data
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
            }
        ]
    };

    const chart = <Chart type="pie" data={chartData} />;

    return (
        <ChartLayout chart={chart} />
    );
};

export default MalfunctionPerVehicleChart;