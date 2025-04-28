import '@styles/Login.css'
import { useContext } from 'react';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { FloatLabel } from 'primereact/floatlabel';
import { PrimeIcons } from 'primereact/api';


import { useNavigate } from '@tanstack/react-router';
import { LoginRequest } from 'types/types';
import { login } from '@api/services/authService';
import { useToast } from '@hooks/useToast';
import AuthContext from '@context/AuthProvider';
import { LOGIN_TOAST_SHOWN } from '@utils/constants/storageKeys';
import { Controller, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { loginSchema } from '@utils/validationSchemas';
import { useAuth } from '@hooks/useAuth';

// Used BEM model for css class names
const Login = () => {
    const { showError } = useToast();
    const navigate = useNavigate({ from: '/login' });
    const { setLoginParameters } = useAuth(); // Using useAuth instead of useContext

    const { control, handleSubmit, formState: { errors }, reset } = useForm({ 
        resolver: yupResolver(loginSchema), defaultValues: { username: '', password: '' }
    });

    const onSubmit = async (data: LoginRequest) => {
        try {
            const response = await login(data);
            
            if (response) { 
                sessionStorage.setItem(LOGIN_TOAST_SHOWN, 'false');
                setLoginParameters(response.jwtToken);
                let role = response.user.role.toLowerCase();

                if (role === 'administrator') 
                    role = 'admin';
                
                navigate({ to: `/${role}` });
            } else {
                showError("Login Failed", "Bad credentials");
            }
        } catch (error) {
            console.error('Login error:', error);
            showError("Login Failed", "Bad credentials"); 
        }
        
        reset();
    };
  
    return (
      <div className={'login'}>
        <div className={'login__container'}>
          <header className={'login__header'}>
            <h1 className={'login__title'}>Login</h1>
          </header>
  
          <form className={'login__form'} onSubmit={handleSubmit(onSubmit)}>
            <div className={'login__input'}>
              <span className="pi pi-user"></span>
              <FloatLabel style={{ width: '100%' }}>
                <Controller
                  name="username"
                  control={control}
                  render={({ field, fieldState }) => (
                    <InputText
                      {...field}
                      className={`login__input--input ${fieldState.error ? 'p-invalid' : ''}`}
                    />
                  )}
                />
                <label htmlFor="username">Enter your username</label>
                {errors.username && <small className="p-error">{errors.username.message}</small>}
              </FloatLabel>
            </div>
  
            <div className={'login__input'}>
              <span className="pi pi-lock"></span>
              <FloatLabel style={{ width: '100%' }}>
                <Controller
                  name="password"
                  control={control}
                  render={({ field, fieldState }) => (
                    <Password
                      {...field}
                      inputStyle={{ width: '100%' }}
                      toggleMask
                      feedback={false}
                      className={fieldState.error ? 'p-invalid' : ''}
                    />
                  )}
                />
                <label htmlFor="password">Enter your password</label>
                {errors.password && <small className="p-error">{errors.password.message}</small>}
              </FloatLabel>
            </div>
  
            <Button
              type="submit"
              label="Login"
              icon={PrimeIcons.SIGN_IN}
              className={`${'login__button'} p-button`}
            />
          </form>
        </div>
      </div>
    );
  }
  
  export default Login;