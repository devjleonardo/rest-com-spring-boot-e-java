import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import "./styles.css";

import api from "../../services/api";

import logoImage from '../../assets/logo.svg'
import padlock from '../../assets/padlock.png'

export default function Login() {
    const [nomeDeUsuario, setNomeDeUsuario] = useState('');
    const [senha, setSenha] = useState('');

    const navigate = useNavigate();

    async function login(e) {
        e.preventDefault();

        const data = {
            nomeDeUsuario,
            senha
        };

        try {
            const response = await api.post("autenticacao/signin", data);

            localStorage.setItem("nomeDeUsuario", nomeDeUsuario);
            localStorage.setItem("accessToken", response.data.accessToken);

            navigate("/livros");
        } catch (error) {
            console.error(error)
            alert("Login falhou! Tente novamente!")
        }
    }

    return (
        <div className="login-container">
            <section className="form">
                <img src={logoImage} alt="Logo" />

                <form onSubmit={login}>
                    <h1>Acesse sua conta</h1>

                    <input  
                        placeholder="Nome de usuário" 
                        value={nomeDeUsuario}
                        onChange={e => setNomeDeUsuario(e.target.value)}
                    />

                    <input 
                        type="password" placeholder="Senha" 
                        value={senha}
                        onChange={e => setSenha(e.target.value)}
                    />

                    <button className="button" type="submit">Login</button>
                </form>
            </section>

            <img src={padlock} alt="Login" />
        </div>
    );
}
