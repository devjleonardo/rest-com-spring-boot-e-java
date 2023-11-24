import React from "react";
import { BrowserRouter, Route, Routes} from "react-router-dom";

import Login from "./pages/Login";
import Livro from "./pages/Livro";

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" exact element={<Login />} />
                <Route path="/livros" element={<Livro />} />
            </Routes>
        </BrowserRouter>
    );
}