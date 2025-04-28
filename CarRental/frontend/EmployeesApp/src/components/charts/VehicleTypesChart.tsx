import { Chart } from 'primereact/chart';

interface VehicleChartProps {
    data: number[];
}

const VehicleTypesChart = ({ data }: VehicleChartProps) => {
    const chartData = {
        labels: ['Car', 'Bicycle', 'Scooter'],
        datasets: [
            {
                data: data, // dynamic data
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
            }
        ]
    };

    return (
        <div>
            <h3>Vehicle Types Distribution</h3>
            <Chart type="pie" data={chartData} />
        </div>
    );
};

export default VehicleTypesChart;