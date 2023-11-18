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

INSERT INTO actions (id, name) VALUES
   (1, 'CREATE'),
   (2, 'READ'),
   (3, 'UPDATE'),
   (4, 'DELETE'),
   (5, 'VIEW');

INSERT INTO resources (id, name) VALUES
   (1, 'USER'),
   (2, 'ROLE');

INSERT INTO permissions (id, resource, action) VALUES
   -- Security
   (1, 'USER', 'CREATE'),
   (2, 'USER', 'READ'),
   (3, 'USER', 'UPDATE'),
   (4, 'USER', 'DELETE'),
   (5, 'ROLE', 'CREATE'),
   (6, 'ROLE', 'READ'),
   (7, 'ROLE', 'UPDATE'),
   (8, 'ROLE', 'DELETE'),
   -- User tokens
   (14, 'USER_TOKEN', 'READ'),
   (15, 'USER_TOKEN', 'UPDATE'),
   (16, 'USER_TOKEN', 'DELETE'),
   -- Security views
   (12, 'USER', 'VIEW'),
   (13, 'ROLE', 'VIEW'),
   (17, 'USER_TOKEN', 'VIEW');
