import React from "react";
import { BrowserRouter, Route, Routes} from "react-router-dom";

import Login from "./pages/Login";
import Livros from "./pages/Livros";
import NovoLivro from "./pages/NovoLivro";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" exact element={<Login />} />
                <Route path="/livros" exact element={<Livros />} />
                <Route path="/livros/novo" element={<NovoLivro />} />
            </Routes>
        </BrowserRouter>
    );
}