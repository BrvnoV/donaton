import axios from 'axios';

const clienteAxios = axios.create({
    baseURL: 'http://localhost:8080/api/bff',
    timeout: 5000
});

export const interceptoresAxios = (setIsLoading, setServiceUnavailable) => {
    clienteAxios.interceptors.request.use(
        (config) => {
            setIsLoading(true);
            return config;
        },
        (error) => {
            setIsLoading(false);
            return Promise.reject(error);
        }
    );

    clienteAxios.interceptors.response.use(
        (response) => {
            setIsLoading(false);
            return response;
        },
        (error) => {
            setIsLoading(false);
            if (error.code === 'ECONNABORTED' || !error.response || error.response.status >= 500) {
                setServiceUnavailable(true);
            }
            return Promise.reject(error);
        }
    );
};

export default clienteAxios;
