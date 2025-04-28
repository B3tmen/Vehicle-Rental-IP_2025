import { Chart } from 'primereact/chart';

interface Props {
    data: number[];
}

const EmployeeRolesChart = ({ data }: Props) => {
    const chartData = {
        labels: ['Administrator', 'Operator', 'Manager'],
        datasets: [
            {
                label: 'Manpower',
                data: data, // dynamic data
                backgroundColor: [
                    'rgba(255, 159, 64, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                ],
                hoverBackgroundColor: [
                    'rgba(255, 159, 64, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(54, 162, 235, 1)',
                ]
            }
        ],

    };

    return (
        <div style={{ width: '100%' }}>
            <h3>Employee roles distribution</h3>
            <Chart type="bar" data={chartData} />
        </div>
    );
};

export default EmployeeRolesChart;