import './App.css';
import { BrowserRouter, Outlet, Route, Routes } from 'react-router';
import Layout from './ui/components/layout/Layout/Layout.tsx';
import HomePage from './ui/pages/home/HomePage/HomePage.tsx';
import RegisterPage from './ui/pages/auth/RegisterPage/RegisterPage.tsx';
import LoginPage from './ui/pages/auth/LoginPage/LoginPage.tsx';
import ProtectedRoute from './ui/components/routing/ProtectedRoute/ProtectedRoute.tsx';
import SearchRunsProvider from './providers/searchRunsProvider.tsx';
import SearchRunsPage from './ui/pages/searchrun/SearchRunsPage/SearchRunsPage.tsx';
import SearchRunDetailsPage from './ui/pages/searchrun/SearchRunDetailsPage/SearchRunDetailsPage.tsx';
import ResourcesPage from './ui/pages/resource/ResourcesPage/ResourcesPage.tsx';
import ResourceDetailsPage from './ui/pages/resource/ResourceDetailsPage/ResourceDetailsPage.tsx';
import DonationsPage from './ui/pages/donation/DonationsPage/DonationsPage.tsx';
import McpPlaygroundPage from './ui/pages/mcp/McpPlaygroundPage/McpPlaygroundPage.tsx';

import NegotiationPage from './ui/pages/negotiation/NegotiationPage/NegotiationPage.tsx';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/register' element={<RegisterPage/>}/>
        <Route path='/login' element={<LoginPage/>}/>
        <Route path='/' element={<Layout/>}>
          <Route index element={<HomePage/>}/>
          <Route element={<ProtectedRoute/>}>
            <Route element={<SearchRunsProvider><Outlet/></SearchRunsProvider>}>
              <Route path='search-runs' element={<SearchRunsPage/>}/>
              <Route path='search-runs/:id' element={<SearchRunDetailsPage/>}/>
            </Route>
            <Route path='resources' element={<ResourcesPage/>}/>
            <Route path='resources/:id' element={<ResourceDetailsPage/>}/>
            <Route path='donations' element={<DonationsPage/>}/>
            <Route path='playground' element={<McpPlaygroundPage/>}/>
              <Route path='negotiation' element={<NegotiationPage/>}/>
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
