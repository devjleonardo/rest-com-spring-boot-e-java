import React, {useState} from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import api from "../../services/api";

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function NovoLivro() {
    const [id, setId] = useState(null);
    const [titulo, setTitulo] = useState('');
    const [autor, setAutor] = useState('');
    const [dataLancamento, setDataLancamento] = useState('');
    const [preco, setPreco] = useState('');

    const {livroId} = useParams();
    
    const accessToken = localStorage.getItem("accessToken");

    const navigate = useNavigate();

    async function criarNovoLivro(e) {
        e.preventDefault();

        const data = {
            titulo,
            autor,
            dataLancamento,
            preco
        };

        try {
            await api.post("api/livros/v1", data, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

            navigate("/livros");
        } catch (error) {
            alert("Erro ao recuperar adicionar o livro ! Tente novamente!");
        }
    }

    return (
        <div className="novo-livro-container">
            <div className="content">
                <section className="form">
                    <img src={logoImage} alt="Logo" />

                    <h1>Adicionar novo livro</h1>

                    <p>Insira as informações do livro e clique em 'Adicionar'! #### {livroId}</p>

                    <Link className="back-link" to="/livros">
                        <FiArrowLeft size={16} color="#251FC5" />
                        Home
                    </Link>
                </section>

                <form onSubmit={criarNovoLivro}>
                    <input
                        placeholder="Título" 
                        value={titulo}
                        onChange={e => setTitulo(e.target.value)}
                    />

                    <input
                        placeholder="Autor" 
                        value={autor}
                        onChange={e => setAutor(e.target.value)}
                    />

                    <input
                        type="date" 
                        value={dataLancamento}
                        onChange={e => setDataLancamento(e.target.value)}
                    />

                    <input
                        placeholder="Preço" 
                        value={preco}
                        onChange={e => setPreco(e.target.value)}
                    />

                    <button className="button" type="submit">Adicionar</button>
                </form>
            </div>
        </div>
    );
}