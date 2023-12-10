--
--  The MIT License (MIT)
--
--  Copyright (c) 2023 Bernardo Martínez Garrido
--  
--  Permission is hereby granted, free of charge, to any person obtaining a copy
--  of this software and associated documentation files (the "Software"), to deal
--  in the Software without restriction, including without limitation the rights
--  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
--  copies of the Software, and to permit persons to whom the Software is
--  furnished to do so, subject to the following conditions:
--  
--  The above copyright notice and this permission notice shall be included in all
--  copies or substantial portions of the Software.
--  
--  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
--  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
--  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
--  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
--  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
--  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
--  SOFTWARE.
--


-- ****************************************
-- This SQL script populates the initial permissions data.
-- ****************************************

INSERT INTO actions (name) VALUES
   ('CREATE'),
   ('READ'),
   ('UPDATE'),
   ('DELETE'),
   ('VIEW');

INSERT INTO resources (name) VALUES
   ('USER'),
   ('ROLE'),
   ('USER_TOKEN'),
   ('LOGIN_REGISTER');

INSERT INTO permissions (name, resource, action) VALUES
   -- Security
   ('USER:CREATE', 'USER', 'CREATE'),
   ('USER:READ', 'USER', 'READ'),
   ('USER:UPDATE', 'USER', 'UPDATE'),
   ('USER:DELETE', 'USER', 'DELETE'),
   ('ROLE:CREATE', 'ROLE', 'CREATE'),
   ('ROLE:READ''ROLE', 'READ'),
   ('ROLE:UPDATE', 'ROLE', 'UPDATE'),
   ('ROLE:DELETE', 'ROLE', 'DELETE'),
   ('LOGIN_REGISTER:READ', 'LOGIN_REGISTER', 'READ'),
   -- User tokens
   ('USER_TOKEN:READ', 'USER_TOKEN', 'READ'),
   ('USER_TOKEN:UPDATE', 'USER_TOKEN', 'UPDATE'),
   ('USER_TOKEN:DELETE', 'USER_TOKEN', 'DELETE'),
   -- Security views
   ('USER:VIEW', 'USER', 'VIEW'),
   ('ROLE:VIEW', 'ROLE', 'VIEW'),
   ('USER_TOKEN:VIEW', 'USER_TOKEN', 'VIEW');
