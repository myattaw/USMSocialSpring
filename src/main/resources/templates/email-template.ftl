<html>
<head>
    <title>${subject}</title>
    <style>
        /* Common styles for all tables */
        .responsive-table {
            cellpadding: 0;
            cellspacing: 0;
            width: 100%;
            background: #ffffff;
            margin: auto; /* Center the table */
        }

        /* Responsive styles for screens less than 1000px */
        @media screen and (max-width: 1000px) {
            .responsive-table {
                width: 100%;
            }
        }

        /* Responsive styles for screens bigger than 1000px */
        @media screen and (min-width: 1001px) {
            .responsive-table {
                width: 50%;
            }
        }
    </style>
</head>
<body style="font-family: Helvetica Neue, Helvetica, Arial, Lucida Grande, sans-serif; margin: 0; padding: 0;">

<!-- Header -->
<table cellpadding="0" cellspacing="0" width="100%" style="background: #002752;">
    <tr>
        <td align="center">
            <img src="https://usm.maine.edu/wp-content/uploads/2022/07/USM_headerLogo.png" alt="USM Logo" width="138"
                 style="display: block; padding: 40px 0;">
        </td>
    </tr>
</table>

<!-- Content -->
<table class="responsive-table">
    <tr>
        <td style="padding: 40px 50px;" align="center">
            <h2 style="font-size: 20px; color: #4f545c;">Hey ${firstName} ${lastName},</h2>
            <p style="color: #737f8d;">${body}</p>
            <a href="${verifyLink}"
               style="background: #002752; color: #f5a800; padding: 15px 19px; border-radius: 3px; text-decoration: none; font-size: 15px;">${buttonText}</a>
        </td>
    </tr>
</table>

<!-- Footer -->
<table cellpadding="0" cellspacing="0" width="100%" style="background: #ffffff;" align="center">
    <tr>
        <td style="padding: 30px 50px;" align="center">
            <p style="border-top: 1px solid #dcddde; font-size: 1px; margin: 0 auto; width: 100%;"></p>
            <p style="color: #747f8d; font-size: 13px;">Need help? Contact us on Twitter <a href="#"
                                                                                            style="color: #f5a800;">@USMSocialProject</a>.
            </p>
            <p style="color: #747f8d; font-size: 12px;">© 2024 University of Southern Maine&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;A
                member of the University of Maine System&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<i
                        class="fas fa-envelope-square"></i>P.O. Box 9300, Portland, ME 04104&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<i
                        class="fas fa-phone-square-alt"></i>1-800-800-4876</p>
        </td>
    </tr>
</table>

</body>
</html>
