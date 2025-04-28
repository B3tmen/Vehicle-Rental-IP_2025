import { Chart } from 'primereact/chart';

interface UserRoleChartProps {
    data: number[];
}

const UserTypesChart = ({ data }: UserRoleChartProps) => {
    const chartData = {
        labels: ['Employees', 'Clients'],
        datasets: [
            {
                data: data, // dynamic data
                backgroundColor: ['#42A5F5', '#66BB6A'],
                hoverBackgroundColor: ['#42A5F5', '#66BB6A']
            }
        ]
    };

    return (
        <div>
            <h3>User Types Distribution</h3>
            <Chart type="pie" data={chartData} />
        </div>
    );
};

export default UserTypesChart;