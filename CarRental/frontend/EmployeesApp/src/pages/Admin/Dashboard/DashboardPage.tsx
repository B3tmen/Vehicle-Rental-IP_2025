import './DashboardPage.css'
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCar, faIndustry, faUsers } from "@fortawesome/free-solid-svg-icons";

import { DashboardCard } from "@components/admin/DashboardCard";
import { ChartContainer } from '@components/admin/ChartContainer';
import VehicleTypesChart from '@components/charts/VehicleTypesChart';
import UserTypesChart from '@components/charts/UserTypesChart';
import EmployeeRolesChart from '@components/charts/EmployeeRolesChart';
import { getEmployeeRolesDistributionMap, getManufacturersQuantity, getUsersQuantity, getUserTypesDistributionMap, getVehiclesQuantity, getVehicleTypesDistributionMap } from '@api/services/dashboardService';
import { useToast } from '@hooks/useToast';
import { LOGIN_TOAST_SHOWN } from '@utils/constants/storageKeys';

function DashboardPage(){
    const { showSuccess } = useToast();
    const [vehicleQuantity, setVehicleQuantity] = useState(0);
    const [manufacturerQuantity, setManufacturerQuantity] = useState(0);
    const [usersQuantity, setUsersQuantity] = useState(0);
    const [vehicleData, setVehiclesData] = useState<number[]>([]); // Car, Bicycle, Scooter counts
    const [userTypesData, setUserTypesData] = useState<number[]>([]); // Clients, Employees counts
    const [employeeRolesData, setEmployeeRolesData] = useState<number[]>([]); 

    const vehicleTypesChart = <VehicleTypesChart key="vehicleChart" data={vehicleData} />;
    const userTypesChart = <UserTypesChart key="userTypesChart" data={userTypesData} />
    const employeeRolesChart = <EmployeeRolesChart key="employeeRolesChart" data={employeeRolesData} />

    const charts = [
        userTypesChart, employeeRolesChart
    ];

    useEffect(() => {
        const hasShownToast = sessionStorage.getItem(LOGIN_TOAST_SHOWN);
        if (hasShownToast === 'false') {
            showSuccess('Login Successful', 'Welcome back!');
            sessionStorage.setItem(LOGIN_TOAST_SHOWN, 'true');
        }

        const fetchQuantities = async () => {
            try{
                const vQuantity = await getVehiclesQuantity();
                const mQuantity = await getManufacturersQuantity();
                const uQuantity = await getUsersQuantity();
                
                setVehicleQuantity(vQuantity);
                setManufacturerQuantity(mQuantity);
                setUsersQuantity(uQuantity);
            } catch(error){
                console.log(error);
            }
        }
        const fetchData = async () => {
            const vehicleTypesMap = await getVehicleTypesDistributionMap();
            const userTypesMap = await getUserTypesDistributionMap();
            const employeeRolesMap = await getEmployeeRolesDistributionMap();

            setVehiclesData(Object.keys(vehicleTypesMap).map(type => vehicleTypesMap[type]));
            setUserTypesData(Object.keys(userTypesMap).map(type => userTypesMap[type]));
            setEmployeeRolesData(Object.keys(employeeRolesMap).map(role => employeeRolesMap[role]));
        }
        
        fetchQuantities();
        fetchData();
    }, [showSuccess]);
    

    return(
        <div className="dashboard">
            <div className="dasboard__title">
                <h2>Dashboard</h2>
            </div>

            <div className="dasboard__content">
                <div className="dasboard__content__cards-container">
                    <DashboardCard title="Vehicles" icon={<FontAwesomeIcon icon={faCar} size='3x' />} quantity={vehicleQuantity} />
                    <DashboardCard title="Manufacturers" icon={<FontAwesomeIcon icon={faIndustry} size='3x' />} quantity={manufacturerQuantity} />
                    <DashboardCard title="Users" icon={<FontAwesomeIcon icon={faUsers} size='3x' />} quantity={usersQuantity} />
                </div>

                <div className="dasboard__content__charts-container">
                    <ChartContainer title='Vehicle statistics' charts={vehicleTypesChart} />
                    <ChartContainer title='User statistics' charts={charts} />
                </div>
            </div>
        </div>
    );
}

export default DashboardPage;