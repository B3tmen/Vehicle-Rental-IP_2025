import '@styles/charts/ChartLayout.css'

interface Props {
    chart: React.ReactNode;
    filters?: React.ReactNode;
}

export const ChartLayout: React.FC<Props> = ({ chart, filters }) => {

    return (
        <div className='chart-layout-container'>
            <div className='chart-layout-container__filters'>
                {filters}
            </div>
            <div className='chart-layout-container__chart'>
                {chart}
            </div>
            
        </div>
    );
};